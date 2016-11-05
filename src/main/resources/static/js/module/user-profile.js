
var UserProfileModule=(function(){
	
	
    var userViewModel;
 
    function init(value){
        initView(value);
        CommonModule.formattHtmlComponents();
    }

    function initView(value){
        userViewModel=kendo.observable({

            modelView: value,
            isVisible : true,
			isEnabled : true,
            onBackToSearchClick: function () {
                CommonModule.navigationStart("#/user.profile");
                CommonModule.navigateTo("#/curso.novedades");
            },
            onSaveClick: function () {
                var filterObj = this.get('modelView');

                if(!validateForm(filterObj)){
                    CommonModule.showNotificationError("Por favor, complete todos los campos requeridos");
                    return false;
                }
                if(!validEmail(filterObj.email)){
                    CommonModule.showNotificationError("Por favor, ingrese un Email valido");
                    return false;
                }

                if(filterObj.password=='') filterObj.password=null;
                if(filterObj.newPassword=='') filterObj.newPassword=null;
                if(filterObj.confirmPassword=='') filterObj.confirmPassword=null;

                if(!validChangePassword(filterObj)){
                    return false;
                }

                var observable = new kendo.data.ObservableObject(filterObj);
                observable.manager = null;

                var json = observable.toJSON();
                var jsonFilter = JSON.stringify(json);

                ServerHandlerModule.callServer("userApi/updateProfile/", "post", jsonFilter, onSaveClickCallBack);
                
            }

        });

        kendo.bind($("#editProfileUser"),userViewModel);
    }


    function validateForm(model) {
        var valid = true;
        if(model.email == null || model.email == ''){
            $("#email").parent().children().addClass("errorCombo");
            valid=false;
        }
        return valid;
    }

    function validData(model){
        var valid = true;

        if(model.confirmPassword!=null && !(model.password!=null && model.password!='') ){
            $("#confirmPassword").parent().children().addClass("errorCombo");
            valid=false;
        }

        return valid;
    }

    function validEmail(email){
        var valid = true;
        var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if(!regex.test(email)){
            $("#email").parent().children().addClass("errorCombo");
            valid=false;
        }

        return valid;
    }

    function validChangePassword(model){
        var valid = true;

        if(!(model.password==null && model.newPassword==null && model.confirmPassword==null)){

            if(model.password==null){
                CommonModule.showNotificationError("El Password actual es requerido");
                valid=false;
            }
            else if(model.newPassword!=null && model.confirmPassword==null){
                CommonModule.showNotificationError("Por favor, confirmar nuevo password");
                valid=false;
            }
            else if(model.password!=null && model.newPassword==null){
                CommonModule.showNotificationError("Por favor, ingresar el nuevo Password o limpiar el campo 'Password'");
                valid = false;
            }
            else if(model.newPassword.length < 6){
                CommonModule.showNotificationError("La longitud mínima del Password es de 6 caracteres");
                valid = false;
            }
            else if(model.newPassword!= model.confirmPassword){
                CommonModule.showNotificationError("Por favor, los passwords deben coincidir");
                valid=false;
            }

        }
        return valid;
    }

 
   function onSaveClickCallBack (result) {
       if(result != -1){
           CommonModule.showNotificationSuccess("El Profile del usuario fue modificado satisfactoriamente");
           CommonModule.navigateTo("#/curso.novedades");
       }
       else{
           CommonModule.showNotificationError("El password que has ingresado no es correcto");
       }
    }
    
    // API
    return {
        init:init
      
    };
})();