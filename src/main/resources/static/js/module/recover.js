var RecoverModule = (function () {
    var viewModel;

    function initModule() {
        initView();
        configHtmlComponents();
        CommonModule.formattHtmlComponents();
    }

    function initView(){
        viewModel = kendo.observable({
            username: null,

            onRecoverClick: function() {

                if(!validateLoginFields(this.get("username"))){
                    return;
                }

                $.ajax({
                    data: JSON.stringify(viewModel.toJSON()),
                    dataType: 'json',
                    contentType: "application/json",
                    timeout: 30000,
                    type: 'POST',
                    url: '/backend/post/security/recover'

                }).done(function(data, textStatus, jqXHR) {
                    console.info("Recovering password successfully!");
                    
                    var data = $.parseJSON(jqXHR.responseText)
                    if (data.status === "ERR"){
                        CommonModule.showNotificationError(data.description);
                    } else {
                        CommonModule.showNotificationSuccess(data.description);
                    
                    }

                }).fail(function(jqXHR, textStatus, errorThrown) {
                    
                });

                console.log("event :: click : submit form ");
            }
        });

        kendo.bind($("#recoverBindArea"), viewModel);
    }

    function validateLoginFields(username){

        if(CommonModule.isBlank(username)){
            CommonModule.showNotificationError("Username is required");
            return false;
        }

        return true;
    }

    function configHtmlComponents(){
    }

    //  API
    return {
        //Init Module
        initModule:initModule,
    };
})();
