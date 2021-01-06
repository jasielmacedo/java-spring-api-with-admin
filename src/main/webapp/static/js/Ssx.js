if(typeof String.prototype.trim !== 'function') {
  String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g, ''); 
  }
}


function SsxJs()
{
	this.confirm = function(callback,text)
	{
		var confirmed_text = "Tem certeza que deseja remover esse item?";
		if(text != undefined)
			confirmed_text = text;
		
		if($('#ssx_dialog_confirm').length < 1)
		{
			var boxDialog = "" +
				"<div id='ssx_dialog_confirm' class='modal fade' data-backdrop='static' data-keyboard='false' tabindex='-1' role='dialog' aria-hidden='true'>" +
				  "<div class='modal-dialog'>"+
				    "<div class='modal-content'>"+
						"<div class='modal-header'>"+
				    		"<h3 class='modal-title'>Confirma&ccedil;&atilde;o</h3>"+
							"<button type='button' class='close' data-dismiss='modal'><span aria-hidden='true'>&times;</span><span class='sr-only'>Close</span></button>"+
				    	"</div>"+
				    	"<div class='modal-body' id='ssx_dialog_confirm_text'>"+
							"Tem certeza que deseja remover esse item?"+
						"</div>"+
						"<div class='modal-footer'>"+
							"<button id='ssx_dialog_confirm_accept' class='btn btn-small btn-success'>Confirmar</button>"+
							"<button id='ssx_dialog_confirm_cancel' class='btn btn-small btn-danger'>Cancelar</button>"+
						"</div>"+
					"</div>"+
				  "</div>"+
				"</div>";
	
			$('body').prepend(boxDialog);			
		}
		
		$('#ssx_dialog_confirm_text').html(confirmed_text);
		
		$('#ssx_dialog_confirm_cancel').click(function()
		{
			$(this).unbind( "click" );
			$('#ssx_dialog_confirm_accept').unbind( "click" );
			$('#ssx_dialog_confirm').modal('hide');
		});
		
		$('#ssx_dialog_confirm_accept').click(function()
		{
			$(this).unbind( "click" );
			$('#ssx_dialog_confirm_cancel').unbind( "click" );
			$('#ssx_dialog_confirm').modal('hide');
			callback();
			callback = null;
		});
		
		$('#ssx_dialog_confirm').modal('show');
	};
	
	this.alert = function(text,callback)
	{
		if($('#ssx_dialog_alert').length < 1)
		{
			var boxDialog = "" +
				"<div id='ssx_dialog_alert' data-backdrop='static' data-keyboard='false' class='modal fade' tabindex='-1' role='dialog' aria-hidden='true'>" +
				  "<div class='modal-dialog'>"+
				    "<div class='modal-content'>"+
						"<div class='modal-header'>"+
							"<h3 class='modal-title'>Aviso</h3>"+
				    		"<button type='button' class='close' data-dismiss='modal'><span aria-hidden='true'>&times;</span><span class='sr-only'>Close</span></button>"+
				    	"</div>"+
				    	"<div class='modal-body' id='ssx_dialog_alert_text'>"+
							"Alerta"+
						"</div>"+
						"<div class='modal-footer'>"+
							"<button id='ssx_dialog_alert_ok' class='btn btn-small btn-info'>Ok</button>"+
						"</div>"+
					"</div>"+
				  "</div>"+
				"</div>";
	
			$('body').prepend(boxDialog);			
		}
		
		$('#ssx_dialog_alert_text').html(text);
		$('#ssx_dialog_alert').modal('show');
		
		$('#ssx_dialog_alert').click(function()
		{
			$(this).unbind( "click" );
			$('#ssx_dialog_alert').modal('hide');
			if(callback != undefined)
				callback();
			callback = null;
		});
	};
	
	this.isEmpty = function(value)
	{
		if(value == "")
			return true;
		return false;
	};
	
	this.isNull = function(value)
	{
		if(value == null)
			return true;
		return false;
	};
	
	this.isEmail = function(value)
	{
		  if(this.isEmpty(value))
			  return false;
		
		  var pattern='([\\w-+]+(?:\\.[\\w-+]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,7})';

	      var p = new RegExp(pattern,["i"]);
	      var m = p.exec(value);
	      
	      if(m != null)
	    	  return true;
	      return false;
	};
	
	this.isCpf = function(cpf)
	{
		  var numeros, digitos, soma, i, resultado, digitos_iguais;
		  
	      digitos_iguais = 1;
	      
	      if (cpf.length < 11)
	            return false;
	      
	      for (i = 0; i < cpf.length - 1; i++)
	              if (cpf.charAt(i) != cpf.charAt(i + 1))
                  {
	            	 digitos_iguais = 0;
	            	 break;
                  }
	      
	        if (!digitos_iguais)
            {
	            numeros = cpf.substring(0,9);
	            digitos = cpf.substring(9);
	            soma = 0;
	            for (i = 10; i > 1; i--)
	                  soma += numeros.charAt(10 - i) * i;
	            resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
	            if (resultado != digitos.charAt(0))
	                  return false;
	            numeros = cpf.substring(0,10);
	            soma = 0;
	            for (i = 11; i > 1; i--)
	                  soma += numeros.charAt(11 - i) * i;
	            resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
	            if (resultado != digitos.charAt(1))
	                  return false;
	            return true;
            }
	        else
	            return false;
	};
	
	this.isCnpj = function(cnpj) {

	    cnpj = cnpj.replace(/[^\d]+/g, '');

	    if (cnpj == '') return false;

	    if (cnpj.length != 14)
	        return false;

	    // Elimina CNPJs invalidos conhecidos
	    if (cnpj == "00000000000000" ||
	        cnpj == "11111111111111" ||
	        cnpj == "22222222222222" ||
	        cnpj == "33333333333333" ||
	        cnpj == "44444444444444" ||
	        cnpj == "55555555555555" ||
	        cnpj == "66666666666666" ||
	        cnpj == "77777777777777" ||
	        cnpj == "88888888888888" ||
	        cnpj == "99999999999999")
	        return false;

	    // Valida DVs
	    tamanho = cnpj.length - 2
	    numeros = cnpj.substring(0, tamanho);
	    digitos = cnpj.substring(tamanho);
	    soma = 0;
	    pos = tamanho - 7;
	    for (i = tamanho; i >= 1; i--) {
	        soma += numeros.charAt(tamanho - i) * pos--;
	        if (pos < 2)
	            pos = 9;
	    }
	    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
	    if (resultado != digitos.charAt(0))
	        return false;

	    tamanho = tamanho + 1;
	    numeros = cnpj.substring(0, tamanho);
	    soma = 0;
	    pos = tamanho - 7;
	    for (i = tamanho; i >= 1; i--) {
	        soma += numeros.charAt(tamanho - i) * pos--;
	        if (pos < 2)
	            pos = 9;
	    }
	    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
	    if (resultado != digitos.charAt(1))
	        return false;

	    return true;

	};
	
	this.isEquals = function(objA, objB)
	{
		if(this.isNull(objA) || this.isNull(objB))
			return false;
		
		if(objA == objB)
			return true;
		return false;
	};
	
	this.charCount = function(value, qtd)
	{		
		if(value.length >= qtd)
			return true;
		return false;
	};
	
	this.isString = function(obj)
	{
		if(typeof obj == 'string')
			return true;
		return false;
	};
	
	this.isNumber = function(obj)
	{
		if(typeof obj == 'number')
			return true;
		return false;
	};
	
	this.isFunction = function isFunction(object) {
		 return jQuery.isFunction(object);
	};
	
	this.validateSubmit = 1;
	
	this.validate = function(form)
	{
		Ssx.validateSubmit = 1;
		
		jQuery(form).find(".required:visible").each(function(index){
			var val = $(this).val().trim(); 
			var type = $(this).attr("data-type");
			var error_point = $(this).attr('data-error');
			var min_length = $(this).attr('data-min');
			
			if(!min_length)
				min_length = 2;
			
			var validField = true;
			
			switch(type)
			{
				case "email":
					if(val == "")
					{
						jQuery(error_point).html("Informe o email").show();
						Ssx.validateSubmit = 0;
						validField = false;
					}else if(!Ssx.isEmail(val))
					{
						jQuery(error_point).html("Email Inválido").show();
						Ssx.validateSubmit = 0;
						validField = false;
					}else{
						jQuery(error_point).html("");
					}
				break;
				case "cpf":
					if(!Ssx.isCpf(val))
					{
						jQuery(error_point).html("Cpf inválido").show();
						Ssx.validateSubmit = 0;
						validField = false;
					}else{
						jQuery(error_point).html("");
					}
				break;
				case "cnpj":
					if(!Ssx.isCnpj(val))
					{
						jQuery(error_point).html("Cpf inválido").show();
						Ssx.validateSubmit = 0;
						validField = false;
					}else{
						jQuery(error_point).html("");
					}
				break;
				case "password":
					var compare = $(this).attr("data-compare");
					
					if(val == "")
					{
						jQuery(error_point).html("Preencha o campo corretamente").show();
						Ssx.validateSubmit = 0;
						validField = false;
					}else if(val.length < min_length)
					{
						jQuery(error_point).html("A senha precisa ter pelo menos "+min_length+" caracteres").show();
						Ssx.validateSubmit = 0;
						validField = false;
					}else if(compare)
					{
						if(val != jQuery(compare).val())
						{
							jQuery(error_point).html("Senhas não conferem").show();
							Ssx.validateSubmit = 0;
							validField = false;
						}else{
							jQuery(error_point).html("");
						}
					}else{
						jQuery(error_point).html("");
					}
				break;
				case "text":
				default:
					if(val == "")
					{
						jQuery(error_point).html("Preencha o campo corretamente").show();
						Ssx.validateSubmit = 0;
						validField = false;
					}else if(jQuery(this).is('input')) 
					{
						if(val.length < min_length)
						{
							jQuery(error_point).html("O Campo precisa ter pelo menos "+min_length+" caracteres").show();
							Ssx.validateSubmit = 0;
							validField = false;
						}else{
							jQuery(error_point).html("");
						}
					}else{
						jQuery(error_point).html("");
					}					
				break;
			}
			
			if(validField)
			{
				$(this).removeClass('is-invalid').addClass('is-valid');
			}else{
				$(this).removeClass('is-valid').addClass('is-invalid');
			}
		});
		
		
		
		return Ssx.validateSubmit;
	};
}

var Ssx = new SsxJs();
window.Ssx = Ssx;

