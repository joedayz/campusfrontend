var ResetPasswordModule = (function () {
    var viewModel;
    var sectkval;
    var minPwdLength;

    function initModule(pMinPwdLength) {
        initView();
        configHtmlComponents();
        CommonModule.formattHtmlComponents();
        sectkval = getParameterByName("sectkval");
        if(CommonModule.isBlank(sectkval)){
            CommonModule.showNotificationError("Invalid Page State, please request another token");
            return false;
        }
        minPwdLength = pMinPwdLength;
    }

    function initView(){
        viewModel = kendo.observable({
            newPassword: null,
            confirmPassword: null,

            onChangeClick: function() {

                if(!validateForm(this.get("newPassword"), this.get("confirmPassword"))){
                    return;
                }

                var data = {
                    newPassword: this.get("newPassword"),
                    confirmPassword: this.get("confirmPassword"),
                    sectkval: sectkval
                }
                $.ajax({
                    data: JSON.stringify(data),
                    timeout: 30000,
                    type: 'POST',
                    url: '/backend/post/security/change-password',
                    dataType: 'json',
                    contentType: "application/json"
                }).done(function(data, textStatus, jqXHR) {
                    console.info("Password changed successfully!");
                    if (data.status === "ERR"){
                        CommonModule.showNotificationError(data.description);
                    } else {
                        CommonModule.showNotificationSuccess(data.description);
                        //window.location = '/';
                    }
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    //console.info("Something goes wrong with the request, please try again later");
                    //CommonModule.showNotificationError('Unable to login');
                });

                console.log("event :: click : submit form ");
            }
        });

        kendo.bind($("#resetBindArea"), viewModel);
    }

    function validateForm(newPassword, confirmPassword){

        if(CommonModule.isBlank(newPassword)){
            CommonModule.showNotificationError("New Password is required");
            return false;
        }

        if(CommonModule.isBlank(confirmPassword)){
            CommonModule.showNotificationError("Confirm Password is required");
            return false;
        }

        if (newPassword !== confirmPassword) {
            CommonModule.showNotificationError("Password doesn't match, please try again");
            return false;
        }

        if (newPassword.length <= minPwdLength) {
            CommonModule.showNotificationError("Password length must be greater than " + minPwdLength + " chars, please try again");
            return false;
        }


        return true;
    }

    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    function configHtmlComponents(){
    }

    //  API
    return {
        //Init Module
        initModule:initModule,
    };
})();
