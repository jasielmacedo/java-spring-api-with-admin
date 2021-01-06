package com.apiadmincore.core.admin.helpers;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormConstruct<T> 
{
	private static final Logger LOG = LoggerFactory.getLogger(FormConstruct.class);
	
	private T element;
	
	private Class<? extends Object> persistentElementClass;
	
	private String buttonSubmitText = "Salvar";
	
	private String errorMsg = "";
	
	public FormConstruct(List<FormField> fields)
	{
		this(fields, null);
	}
	
	private Map<String,FormField> fields;
	
	public FormConstruct(List<FormField> fields, T element)
	{
		this.fields = new LinkedHashMap<String, FormField>();
		
		if(fields.size() > 0)
		{
			for(FormField formItem : fields)
			{
				this.fields.put(formItem.getName().toLowerCase(), formItem);
			}
		}
		
		
		this.SetElement(element);
	}
	
	public void SetElement(T element)
	{
		this.element = element;
		
		if(this.element != null)
		{				
			this.persistentElementClass = element.getClass();
			
			 Method[] methods =  this.persistentElementClass.getDeclaredMethods();

             for (Method m: methods)
             {
            	 try {
	                 if(m.getName().startsWith("get")) 
	                 {
	                     String fieldName = m.getName().replace("get", "").toLowerCase();
	                     if(this.fields.containsKey(fieldName))
	                     {
	                    	 String val = (String)m.invoke(element);
	                    	 this.setFieldValue(fieldName, val);
	                     }
	                 }
	            }catch(Exception e)
	     		{
	     			LOG.error("getter Method is not valid "+m.getName());
	     		}
             }
		}
		
	}
	
	public final void setFieldValue(String field, String value)
	{
		field = field.toLowerCase();
		if(this.fields.containsKey(field))
			this.fields.get(field).setValue(value);
	}
	
	public final void setFieldEnabled(String field, boolean isEnabled)
	{
		field = field.toLowerCase();
		if(this.fields.containsKey(field))
			this.fields.get(field).setEnabled(isEnabled);
	}
	
	public final void setFieldOptions(String field, Map<String,String> options)
	{
		field = field.toLowerCase();
		if(this.fields.containsKey(field))
		{
			this.fields.get(field).setOptions(options);
		}
	}
	
	/**
	 * Seta um campo para ser comparado na validacao. Só funciona com campos de password
	 * @param field
	 * @param fieldToCompare
	 */
	public final void setCompare(String field, String fieldToCompare)
	{
		field = field.toLowerCase();
		if(this.fields.containsKey(field))
		{
			this.fields.get(field).setCompare(fieldToCompare.toLowerCase());
		}
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	
	public final void clearErrorMsg()
	{
		this.errorMsg = "";
	}
	
	public final void clearFieldValues()
	{
		for(Map.Entry<String, FormField> entry : fields.entrySet())
		{
			fields.get(entry.getKey()).setValue("");
		}
	}
	
	public final String getButtonSubmitText() {
		return buttonSubmitText;
	}

	public final void setButtonSubmitText(String buttonSubmitText) {
		this.buttonSubmitText = buttonSubmitText;
	}

	public final String Draw()
	{
		String result = "";
		if(this.fields.size() == 0)
			return result;
		
		if(this.errorMsg != "")
		{
			result += "<div class=\"alert alert-dismissable alert-danger\"><button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button><span>"+this.errorMsg+"</span></div>";
		}
		
		for(Map.Entry<String, FormField> entry : fields.entrySet())
		{
			EFormFieldType type = entry.getValue().getType();
			FormField field = entry.getValue();
			if(type != EFormFieldType.hidden)
			{
				if(field.isEnabled())
				{
					field.setName(field.getName().toLowerCase());
					result += "<div class=\"form-group\">";
					result += "<label for=\"field_"+field.getName()+"\" for=\"inputGroupSelect01\" class=\"label\">"+field.getLabel()+"</label>";
					
					String lengthMin = field.getMin() > 0?"data-min=\""+field.getMin()+"\"":"";
					
					switch(type)
					{
						case textarea:
							result += "<textarea name=\""+field.getName()+"\" id=\"field_"+field.getName()+"\" class=\"form-control "+(field.isRequired()?"required":"")+"\" data-error='#field_"+field.getName()+"_error' "+lengthMin+">"+field.getValue()+"</textarea>";
						break;
						case password:
							String compare = field.getCompare() != null && !field.getCompare().isEmpty()?"data-compare='#field_"+field.getCompare()+"'":"";
							
							result += "<input type=\""+type.toString()+"\" name=\""+field.getName()+"\" id=\"field_"+field.getName()+"\" class=\"form-control "+(field.isRequired()?"required":"")+"\" value=\""+field.getValue()+"\" "+(field.isRequired()?"required":"")+" data-type='"+type+"' data-error='#field_"+field.getName()+"_error' "+lengthMin+" "+compare+"/>";
						break;
						case select:
							result += "<select name='"+field.getName()+"' id='field_"+field.getName()+"' class='form-control "+(field.isRequired()?"required":"")+"' data-type='"+type+"' data-error='#field_"+field.getName()+"_error' "+(field.isRequired()?"required":"")+">";
							result += "<option value=''>-- Selecione</option>";
							for(Map.Entry<String, String> fOption : field.options.entrySet())
							{
								result += "<option value='"+fOption.getKey()+"' "+(field.getValue().equals(fOption.getKey())?"selected='selected'":"")+">"+fOption.getValue()+"</option>";
							}
							result += "</select>";
						break;
						default:
							
							result += "<input type=\""+type.toString()+"\" name=\""+field.getName()+"\" id=\"field_"+field.getName()+"\" class=\"form-control "+(field.isRequired()?"required":"")+"\" value=\""+field.getValue()+"\" "+(field.isRequired()?"required":"")+" data-type='"+type+"' data-error='#field_"+field.getName()+"_error' "+lengthMin+" />";
							
						break;
					}
					
					result += "<div id=\"field_"+field.getName()+"_error\" class=\"invalid-feedback\"></div>";
					result += " </div>";
					
					
				}
			}else {
				result += "<input type=\"hidden\" name=\""+field.getName()+"\" id=\"field_"+field.getName()+"\" value=\""+field.getValue()+"\" />";
			}	
		}
		
		result += "<button type=\"submit\" class=\"btn btn-success btn-md\">"+this.buttonSubmitText+"</button>";
		this.clearFieldValues();
		return result;
	}
	
	public static final String DrawJs()
	{
		return "jQuery(document).ready(function ($) {$('.mask').each(function(){$(this).mask($(this).attr('data-mask'));});$('form').submit(function(){if(Ssx.validate(this))return true;return false;});});";
	}
}
