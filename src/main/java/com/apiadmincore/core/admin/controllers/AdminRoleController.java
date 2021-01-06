package com.apiadmincore.core.admin.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import com.apiadmincore.core.admin.payload.DTORole;
import com.apiadmincore.core.admin.payload.DTOUser;
import com.apiadmincore.core.admin.payload.DTOUserPassword;
import com.apiadmincore.core.models.ERole;
import com.apiadmincore.core.models.Role;
import com.apiadmincore.core.models.User;
import com.apiadmincore.core.services.RoleService;
import com.apiadmincore.core.services.UserService;

@Controller
@RequestMapping("/admin/role")
public class AdminRoleController 
{
	private static final Logger LOG = LoggerFactory.getLogger(AdminRoleController.class);
	
	private RoleService roleService;
	
	private FormConstruct<Role> form;
	
	
	@Autowired
	public AdminRoleController(RoleService roleService, UserService userService)
	{
		this.roleService = roleService;
		
		ArrayList<FormField> listFields = new ArrayList<FormField>();
		listFields.add(new FormField("name", "Nome",EFormFieldType.text, "", true));
		listFields.add(new FormField("role", "Tipo",EFormFieldType.select, "", true));
		
		this.form = new FormConstruct<Role>(listFields);

		LinkedHashMap<String, String> roleOptions = new LinkedHashMap<String, String>();
		for(ERole r : ERole.values())
		{
			roleOptions.put(r.toString(), r.toString());
		}
		
		this.form.setFieldOptions("role", roleOptions);
	}
	
	@GetMapping(value = {"/",""})
	public final String Index(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "s", defaultValue = "") String search, Model model)
	{
		page = Math.max(1, Math.min(9999, page));
		long limit = 20;
		
		List<Role> roles = this.roleService.list(page, limit, search, Sort.Direction.ASC, "name");
		
		model.addAttribute("all",roles);
		
		List<Integer> pagination = Pagination.mountPagination(roleService.count(search), limit);
		pagination = Pagination.pg2pg(pagination, 4, page);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("currentPage", page);
		model.addAttribute("search",search);
		
		return "role/list";
	}
	
	@GetMapping(value = "/{id}")
	public final String View(@PathVariable("id") String id, Model model)
	{
		
		Role currentRole = this.roleService.findById(id);
		if(currentRole == null)
		{
			return "redirect:/admin/role";
		}
		
		model.addAttribute("data",currentRole);
		
		return "role/view";
	}
	
	
	@GetMapping(value = "/edit")
	public final String Add(Model model)
	{	
		this.form.clearErrorMsg();
		this.form.clearFieldValues();
		
		model.addAttribute("formConstruct",this.form.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		
		return "role/edit";
	}
	
	@PostMapping("/edit")
	public final String Add(@Valid DTORole user, BindingResult result, Model model)
	{
		Role roleFinal = user.buildRole();
		
		this.form.clearErrorMsg();
		
		if(result.hasErrors())
		{
			this.form.setErrorMsg("Preencha os campos corretamente");
		}else {
			roleFinal = this.roleService.Create(roleFinal);			
			return "redirect:/admin/role/"+roleFinal.getId();
		}
		
		this.form.SetElement(roleFinal);
		this.form.setFieldValue("role", user.getRole());
		
		model.addAttribute("formConstruct",this.form.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		
		return "role/edit";
	}
	

	@GetMapping(value = "/edit/{id}")
	public final String Edit(@PathVariable("id") String id, Model model)
	{	
		Role currentRole = this.roleService.findById(id);
		if(currentRole == null)
		{
			return "redirect:/admin/role/edit";
		}
		
		this.form.clearErrorMsg();
		
		this.form.setFieldEnabled("password", false);
		this.form.SetElement(currentRole);
		this.form.setFieldValue("role", currentRole.getRole().toString());
		
		model.addAttribute("formConstruct",this.form.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		model.addAttribute("id", currentRole.getId());
		return "role/edit";
	}
	
	@PostMapping("/edit/{userId}")
	public final String Edit(@PathVariable("userId") String id,@Valid DTORole dtoRole, BindingResult result, Model model)
	{
		Role currentRole = this.roleService.findById(id);
		if(currentRole == null)
		{
			return "redirect:/admin/role/edit";
		}
		
		Role roleFinal = currentRole;
		roleFinal.setName(dtoRole.getName());
		roleFinal.setRole(dtoRole.getRoleEnum());
		
		this.form.clearErrorMsg();
		
		if(result.hasErrors())
		{
			this.form.setErrorMsg("Preencha os campos corretamente "+result.getErrorCount());
			
		}else 
		{
			roleFinal = this.roleService.Update(roleFinal);
			return "redirect:/admin/role/"+roleFinal.getId();			
		}
		
		this.form.SetElement(roleFinal);
		this.form.setFieldValue("role", dtoRole.getRole());
		
		this.form.setFieldEnabled("password", false);
		model.addAttribute("formConstruct",this.form.Draw());
		model.addAttribute("formConstructJs", FormConstruct.DrawJs());
		model.addAttribute("id", currentRole.getId());
		return "role/edit";
	}
	
}
