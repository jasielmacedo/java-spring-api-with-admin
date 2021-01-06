package com.apiadmincore.core.admin.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apiadmincore.core.admin.helpers.EFormFieldType;
import com.apiadmincore.core.admin.helpers.FormConstruct;
import com.apiadmincore.core.admin.helpers.FormField;
import com.apiadmincore.core.admin.helpers.Pagination;
import com.apiadmincore.core.admin.payload.DTOUser;
import com.apiadmincore.core.admin.payload.DTOUserPassword;
import com.apiadmincore.core.models.Role;
import com.apiadmincore.core.models.User;
import com.apiadmincore.core.security.services.UserDetailsImpl;
import com.apiadmincore.core.services.RoleService;
import com.apiadmincore.core.services.UserService;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController 
{
	private static final Logger LOG = LoggerFactory.getLogger(AdminUserController.class);
	
	private RoleService roleService;
	
	private UserService userService;
	
	private FormConstruct<User> form;
	
	private FormConstruct<DTOUserPassword> passForm;
	
	
	@Autowired
	public AdminUserController(RoleService roleService, UserService userService)
	{
		this.roleService = roleService;
		this.userService = userService;
		
		ArrayList<FormField> listFields = new ArrayList<FormField>();
		listFields.add(new FormField("name", "Nome",EFormFieldType.text, "", true));
		listFields.add(new FormField("username", "Usuário",EFormFieldType.text, "", true));
		listFields.add(new FormField("email", "Email",EFormFieldType.email, "", true));
		listFields.add(new FormField("role", "Perfil",EFormFieldType.select, "", true));
		listFields.add(new FormField("password", "Senha",EFormFieldType.password, "", true));
		
		this.form = new FormConstruct<User>(listFields);
		
		List<Role> roles =  this.roleService.getAll();
		LinkedHashMap<String, String> roleOptions = new LinkedHashMap<String, String>();
		for(Role r : roles)
		{
			roleOptions.put(r.getId(), r.getName());
		}
		
		this.form.setFieldOptions("role", roleOptions);
		
		
		ArrayList<FormField> listFieldsPass = new ArrayList<FormField>();
		listFieldsPass.add(new FormField("oldPassword", "Senha atual",EFormFieldType.password, "", true));
		listFieldsPass.add(new FormField("newPassword", "Nova senha",EFormFieldType.password, "", true));
		listFieldsPass.add(new FormField("confirmPassword", "Confirmar senha",EFormFieldType.password, "", true));
		
		this.passForm = new FormConstruct<DTOUserPassword>(listFieldsPass);
		this.passForm.setCompare("confirmPassword", "newPassword");
	}
	
	@GetMapping(value = {"/",""})
	public final String Index(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "s", defaultValue = "") String search, Model model)
	{
		page = Math.max(1, Math.min(9999, page));
		long limit = 20;
		
		List<User> users = this.userService.list(page, limit, search, Sort.Direction.ASC, "name");
		
		model.addAttribute("all",users);
		
		List<Integer> pagination = Pagination.mountPagination(userService.count(search), limit);
		pagination = Pagination.pg2pg(pagination, 4, page);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("currentPage", page);
		model.addAttribute("search",search);
		
		return "user/list";
	}
	
	@GetMapping(value = "/{id}")
	public final String View(@PathVariable("id") String id, Model model)
	{
		
		User currentUser = this.userService.findById(id);
		if(currentUser == null)
		{
			return "redirect:/admin/user";
		}
		
		model.addAttribute("data",currentUser);
		
		return "user/view";
	}
	
	
	@GetMapping(value = "/edit")
	public final String Add(Model model)
	{	
		this.form.clearErrorMsg();
		this.form.clearFieldValues();
		this.form.setFieldEnabled("password", true);
		
		model.addAttribute("formConstruct",this.form.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		
		return "user/edit";
	}
	
	@PostMapping("/edit")
	public final String Add(@Valid DTOUser user, BindingResult result, Model model)
	{
		User userFinal = user.buildUser();
		
		this.form.clearErrorMsg();
		
		if(result.hasErrors())
		{
			this.form.setErrorMsg("Preencha os campos corretamente");
		}else {
			if(user.getPassword() == null || user.getPassword().isEmpty())
			{
				this.form.setErrorMsg("informe o password");
			}if(this.userService.existsByUsername(user.getUsername()))
			{
				this.form.setErrorMsg("Nome de usuário já utilizado");
			}else if(this.userService.existsWithEmail(user.getEmail()))
			{
				this.form.setErrorMsg("Email já cadastrado");
			}else {
				
				Role r = this.roleService.findById(user.getRole());
				
				if(r == null)
				{
					this.form.setErrorMsg("Perfil selecionado não existe");
				}else {
					userFinal.setRole(r);
					userFinal = this.userService.CreateWithoutRole(userFinal);
					
					return "redirect:/admin/user/"+userFinal.getId();
				}
			}			
		}
		
		this.form.SetElement(userFinal);
		this.form.setFieldValue("role", user.getRole());
		this.form.setFieldEnabled("password", true);
		
		model.addAttribute("formConstruct",this.form.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		
		return "user/edit";
	}
	

	@GetMapping(value = "/edit/{id}")
	public final String Edit(@PathVariable("id") String id, Model model)
	{	
		User currentUser = this.userService.findById(id);
		if(currentUser == null)
		{
			return "redirect:/admin/user/edit";
		}
		
		this.form.clearErrorMsg();
		
		this.form.setFieldEnabled("password", false);
		this.form.SetElement(currentUser);
		this.form.setFieldValue("role", currentUser.getRole().getId());
		
		model.addAttribute("formConstruct",this.form.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		model.addAttribute("id", currentUser.getId());
		return "user/edit";
	}
	
	@PostMapping("/edit/{userId}")
	public final String Edit(@PathVariable("userId") String id,@Valid DTOUser dtoUser, BindingResult result, Model model)
	{
		User currentUser = this.userService.findById(id);
		if(currentUser == null)
		{
			return "redirect:/admin/user/edit";
		}
		
		User userFinal = currentUser;
		userFinal = dtoUser.updateUser(currentUser);
		
		this.form.clearErrorMsg();
		
		if(result.hasErrors())
		{
			this.form.setErrorMsg("Preencha os campos corretamente "+result.getErrorCount());
			
		}else {
			
			if(!currentUser.getUsername().equals(dtoUser.getUsername()) && this.userService.existsByUsername(dtoUser.getUsername()))
			{
				this.form.setErrorMsg("Nome de usuário já utilizado");
			}else if(!currentUser.getEmail().equals(dtoUser.getEmail()) && this.userService.existsWithEmail(dtoUser.getEmail()))
			{
				this.form.setErrorMsg("Email já cadastrado para outro usuário");
			}else {
				
				Role r = this.roleService.findById(dtoUser.getRole());
				
				if(r == null)
				{
					this.form.setErrorMsg("Perfil selecionado não existe");
				}else {
					userFinal.setRole(r);
					userFinal = this.userService.Update(userFinal);
					
					return "redirect:/admin/user/"+userFinal.getId();
				}
			}			
		}
		
		this.form.SetElement(userFinal);
		this.form.setFieldValue("role", dtoUser.getRole());
		
		this.form.setFieldEnabled("password", false);
		model.addAttribute("formConstruct",this.form.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		model.addAttribute("id", currentUser.getId());
		return "user/edit";
	}
	
	@GetMapping("/pass")
	public final String EditPass(Model model)
	{
		this.passForm.clearErrorMsg();
		this.passForm.clearFieldValues();		
		
		model.addAttribute("formConstruct",this.passForm.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		
		return "user/edit-pass";
	}
	
	@PostMapping("/pass")
	public final String EditPass(@Valid DTOUserPassword userPassword,BindingResult result, Model model)
	{
		if(SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
		{
			return "redirect:/admin/auth/login";
		}
		
		UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(result.hasErrors())
		{
			this.passForm.setErrorMsg("Preencha os campos corretamente");
		}else {
			LOG.error(userDetails.getId());
			if(userDetails.getPassword() != userPassword.getOldPassword())
			{
				this.passForm.setErrorMsg("Senha atual não confere.");
			}else {
				
			}
		}
		
		this.passForm.clearErrorMsg();
		this.passForm.clearFieldValues();		
		
		model.addAttribute("formConstruct",this.passForm.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		
		return "user/edit-pass";
	}
}
