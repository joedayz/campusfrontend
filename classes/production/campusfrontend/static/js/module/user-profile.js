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
                CommonModule.navigateTo("#/rfq.pending");
            },
            onSaveClick: function () {
                var filterObj = this.get('modelView');

                if(!validateForm(filterObj)){
                    CommonModule.showNotificationError("Please fill in all the required fields");
                    return false;
                }
                if(!validEmail(filterObj.email)){
                    CommonModule.showNotificationError("Please enter a valid Email");
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

        if((model.manager!=null && model.manager!='') && model.managerId==null){
            $("#manager").parent().children().addClass("errorCombo");
            valid=false;
        }

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
                CommonModule.showNotificationError("Current Password is required");
                valid=false;
            }
            else if(model.newPassword!=null && model.confirmPassword==null){
                CommonModule.showNotificationError("Please confirm new Password");
                valid=false;
            }
            else if(model.password!=null && model.newPassword==null){
                CommonModule.showNotificationError("Please enter the new Password or clean 'Password' field");
                valid = false;
            }
            else if(model.newPassword.length < 6){
                CommonModule.showNotificationError("Minimum Length of the Password is 6 characters");
                valid = false;
            }
            else if(model.newPassword!= model.confirmPassword){
                CommonModule.showNotificationError("Please enter matching passwords");
                valid=false;
            }

        }
        return valid;
    }

 
   function onSaveClickCallBack (result) {
       if(result != -1){
           CommonModule.showNotificationSuccess("User Profile was edited successfully");
           CommonModule.navigateTo("#/rfq.pending");
       }
       else{
           CommonModule.showNotificationError("The password you have entered is not correct");
       }
    }
    
    // API
    return {
        init:init
      
    };
})();