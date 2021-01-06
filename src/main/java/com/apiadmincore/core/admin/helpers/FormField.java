package com.apiadmincore.core.admin.helpers;

import java.util.Map;

public class FormField
{
	
	private String name;
	
	private String label;

	private EFormFieldType type;
	
	private String value;
	
	Map<String,String> options;
	
	private boolean required;
	
	private String error;
	
	private boolean enabled = true;
	
	private int min;
	
	private String mask;
	
	private String compare;
	
	
	/**
	 * Modelo de construcao de campos com required = false
	 * 
	 * @param name Nome do campo sera usado no processamento do campo
	 * @param label Nome da chamada do campo visivel para o usuario
	 * @param type Tipo de campo
	 * @param value Valor atual do campo
	 */
	public FormField(String name, String label, EFormFieldType type, String value)
	{
		this(name,label,type,value,false);
	}
	
	/**
	 * Modelo de construcao de campos padrao
	 * 
	 * @param name Nome do campo sera usado no processamento do campo
	 * @param label Nome da chamada do campo visivel para o usuario
	 * @param type Tipo de campo
	 * @param value Valor atual do campo
	 * @param required Se for igual a true, o campo não poderá ser salvo sem estar preenchido
	 */
	public FormField(String name, String label, EFormFieldType type, String value, boolean required)
	{
		this.name = name;
		this.label = label;
		this.type = type;
		this.value = value;
		this.required = required;
		this.enabled = true;
		this.error = "Preencha o campo corretamente";
		this.min = 2;
		this.mask = "";
	}
	
	/**
	 * Constroi campo com Mask e Length no final
	 * 
	 * @param name Nome do campo sera usado no processamento do campo
	 * @param label Nome da chamada do campo visivel para o usuario
	 * @param type Tipo de campo
	 * @param value Valor atual do campo
	 * @param required Se for igual a true, o campo não poderá ser salvo sem estar preenchido
	 * @param mask Mascara Js para o Campo (usando jquery.maskedinput) Pode ser vazio
	 * @param minLength Mínino de caracteres exigido pelo campo
	 */
	public FormField(String name, String label, EFormFieldType type, String value, boolean required, String mask, int minLength)
	{
		this(name,label,type,value,required);
		this.mask = mask;
		this.min = minLength;
	}
	
	/**
	 * Constroi campo com opcoes de selecao, util para select e checkbox
	 * 
	 * @param name Nome do campo sera usado no processamento do campo
	 * @param label Nome da chamada do campo visivel para o usuario
	 * @param type Tipo de campo
	 * @param value Valor atual do campo
	 * @param required Se for igual a true, o campo não poderá ser salvo sem estar preenchido
	 * @param options Opcoes que sera utilizadas (Chave String, Valor String)
	 */
	public FormField(String name, String label, EFormFieldType type, String value, boolean required, Map<String,String> options)
	{
		this(name,label,type,value,required);
		this.options = options;
	}
	
	/**
	 * Constro um campo no tipo hidden com required = false
	 * @param name Nome do campo sera usado no processamento do campo
	 * @param value Valor atual do campo
	 */
	public FormField(String name, String value)
	{
		this(name,"",EFormFieldType.hidden,value,false);
	}
	
	public FormField(String name, String label, EFormFieldType type, String value,boolean required, String error)
	{
		this(name,label,type,value,required);
		this.error = error;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}
	
	public final String getLabel() {
		return label;
	}

	public final void setLabel(String label) {
		this.label = label;
	}

	public final EFormFieldType getType() {
		return type;
	}

	public final void setType(EFormFieldType type) {
		this.type = type;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}

	public final Map<String, String> getOptions() {
		return options;
	}

	public final void setOptions(Map<String, String> options) {
		this.options = options;
	}

	public final boolean isRequired() {
		return required;
	}

	public final void setRequired(boolean required) {
		this.required = required;
	}

	public final String getError() {
		return error;
	}

	public final void setError(String error) {
		this.error = error;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getCompare() {
		return compare;
	}

	public void setCompare(String compare) {
		this.compare = compare;
	}
	
	
}