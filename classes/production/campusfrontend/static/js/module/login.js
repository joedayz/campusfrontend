var LoginModule = (function () {
    var loginViewModel;

    function initModule() {
        initView();
        configHtmlComponents();
        CommonModule.formattHtmlComponents();
        initRouter();
    }

    function initRouter (){

            Sammy('#loginArea', function() {
                this.get('#/', function(context) {
                    console.log("Initializing routing system ... ");
                });

                this.get('#/recover' , function(context) {
                    var path = context["path"];
                    var targetUrl = path.substring(context["path"].lastIndexOf("/"));
                    console.log("Routing to " + targetUrl);
                    context.render(targetUrl, {}, function(view) {
                        console.log(view);
                        $('#loginArea').html(view);
                    });
                });

                this.get('#/login' , function(context) {
                    window.location = '/';
                });


            }).run('#/');

    }

    function initView(){
        loginViewModel = kendo.observable({
            username: null,
            password: null,
            rememberMe: null,
            isVisible: true,
            isEnabled: true,
            onCleanClick: function() {
                this.set("username", null);
                this.set("password", null);
            },
            onLoginClick: function() {
                var userName =  $("#username").val();
                var password =  $("#password").val();

                if(!validateLoginFields(userName, password)){
                    return;
                }

                var data = 'username=' + userName + '&password=' + password;
                data += this.get("rememberMe") != null ? '&remember-me=Yes' : "";
                $.ajax({
                    data: data,
                    timeout: 30000,
                    type: 'POST',
                    url: '/login',

                }).done(function(data, textStatus, jqXHR) {
                    console.info("Auth Request Success");
                    if (textStatus === "success") {
                        console.info("Authentication Success!");
                        var returnTo = getUrlParameter("returnTo");
                        if (returnTo == null){
                            window.location = '/';
                        } else {
                            window.location = '/#' + returnTo;
                        }

                    }
                    else {
                        console.error("Unable to login");
                        CommonModule.showNotificationError('Unable to login');
                    }

                }).fail(function(jqXHR, textStatus, errorThrown) {
                    console.info("Something goes wrong with the request");
                    var data = $.parseJSON(jqXHR.responseText)
                    CommonModule.showNotificationError(data.message);
                });

                console.log("event :: click : submit form ");
            }
        });

        kendo.bind($("#loginBindArea"), loginViewModel);
    }

    var getUrlParameter = function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    };

    function validateLoginFields(username, password){

        if(CommonModule.isBlank(username)){
            CommonModule.showNotificationError("Please enter your authentication details, username is required");
            return false;
        }

        if(CommonModule.isBlank(password)){
            CommonModule.showNotificationError("Please enter your authentication details, password is required");
            return false;
        }

        return true;
    }

    function configHtmlComponents(){
        $("#username").keypress(function(event) {
            if (event.which == 13) {
                event.preventDefault();
                $("#btnLogin").click();
            }
        });
        $("#password").keypress(function(event) {
            if (event.which == 13) {
                event.preventDefault();
                $("#btnLogin").click();
            }
        });
    }

    //  API
    return {
        //Init Module
        initModule:initModule,
    };
})();
