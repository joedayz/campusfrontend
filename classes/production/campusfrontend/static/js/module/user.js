var UserModule=(function(){

    var userViewModel;
    var editMode;

    var dataTableOfficeFields = {
        name: { type: "string" },
        state: { type: "string" },
        city: { type: "string" },
    };

    var dataTableRoleFields = {
        roleName: { type: "string" }
       
    };
 
    function init(value,isEdit){
        editMode=isEdit;
        initView(value);
        configHtmlComponents();
        CommonModule.formattHtmlComponents();

        initializeTemps(value);
        GridModule.dataTableDsConfig($("#officesTable"),dataTableOfficeFields,'/userApi/searchOffices',[],false);
        GridModule.dataTableDsConfig($("#rolesTable"),dataTableRoleFields,'/userApi/searchRoles',[],false);

    }

    function initView(value){
        userViewModel=kendo.observable({

            modelView: value,
            isVisible : true,
			isEnabled : true,
            onBackToSearchClick: function () {
                ServerHandlerModule.callServer("userApi/cleanTemps/", "post", []);
                CommonModule.navigationStart("#/user.new");
                CommonModule.navigateTo("#/user.search");
            },
            onSaveClick: function () {
                var filterObj = this.get('modelView');

                if(!validData(filterObj)){
                    CommonModule.showNotificationError("Please clean Invalid Data in the form.");
                    return false;
                }
                if(!validateForm(filterObj)){
                    CommonModule.showNotificationError("Please fill in all the required fields");
                    return false;
                }

                if(!validEmail(filterObj.email)){
                    CommonModule.showNotificationError("Please enter a valid Email");
                    return false;
                }

                if(!validChangePassword(filterObj.password,filterObj.confirmPassword)){
                          return false;
                }

                if(!validOfficesRoles()){
                    return false;
                }

                var observable = new kendo.data.ObservableObject(filterObj);
                observable.manager = null;

                var json = observable.toJSON();
                var jsonFilter = JSON.stringify(json);

                ServerHandlerModule.callServer("userApi/create/", "post", jsonFilter, onSaveClickCallBack);
                 
            },
            onAddOfficeClick:function(){

                var model = this.get('modelView');
                var observable = new kendo.data.ObservableObject(model);

                if(observable.officeId==null){
                    $("#office").parent().children().addClass("errorCombo");
                    CommonModule.showNotificationError("Please select an Office");
                    return false;
                }

                ServerHandlerModule.callServer("/userApi/addOffice","get",{officeId:observable.officeId},function(result){
                    if(result.status=="OK"){
                        GridModule.dataTableDsConfig($("#officesTable"),dataTableOfficeFields,'/userApi/searchOffices',[],false);
                    }else{
                        CommonModule.showNotificationError(result.description);
                    }

                });
            },
            onAddRoleClick:function(){
                var model = this.get('modelView');
                var observable = new kendo.data.ObservableObject(model);
                if(observable.roleId==null){
                    $("#role").parent().children().addClass("errorCombo");
                    CommonModule.showNotificationError("Please select a Role");
                    return false;
                }
                var selectedRole= $("#role").data("kendoDropDownList").dataItem();
               
                ServerHandlerModule.callServer("/userApi/addRole","get",{roleId:observable.roleId,roleName:selectedRole.roleName},function(result){
                    if(result.status=="OK"){
                        GridModule.dataTableDsConfig($("#rolesTable"),dataTableRoleFields,'/userApi/searchRoles',[],false);
                    }else{
                        CommonModule.showNotificationError(result.description);
                    }

                });

            }

        });

        kendo.bind($("#addUser"),userViewModel);
    }


    function validateForm(model) {
        var valid = true;

        if(model.userName == null || model.userName == ''){
            $("#userName").parent().children().addClass("errorCombo");
            valid=false;
        }
        if(model.managerId == null){
            $("#manager").parent().children().addClass("errorCombo");
            valid=false;
        }
        if(model.firstName == null || model.firstName == ''){
            $("#firstName").parent().children().addClass("errorCombo");
            valid=false;
        }
        if(model.lastName == null || model.lastName == ''){
            $("#lastName").parent().children().addClass("errorCombo");
            valid=false;
        }
        if(model.email == null || model.email == ''){
            $("#email").parent().children().addClass("errorCombo");
            valid=false;
        }
        if(model.password == null || model.password == ''){
            $("#password").parent().children().addClass("errorCombo");
            valid=false;
        }
        if(model.confirmPassword == null || model.confirmPassword == ''){
            $("#confirmPassword").parent().children().addClass("errorCombo");
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

    function validChangePassword(pwd, pwdConfirm){
        var valid = true;

        if(pwd.valueOf()!= pwdConfirm.valueOf()){
            CommonModule.showNotificationError("Confirm Password doesn't match Password");
            valid=false;
        }

        else if(pwd.length < 6){
            CommonModule.showNotificationError("Minimum Length of the password is 6 characters");
            valid = false;
        }

        return valid;
    }

    function validOfficesRoles(){
        var valid = true;
        var offices=$("#officesTable").data("kendoGrid").dataSource.view();
        var roles=$("#rolesTable").data("kendoGrid").dataSource.view();

        if(offices.length < 1 && roles.length < 1){
            CommonModule.showNotificationError("Please assign at least one Office and Role");
            valid=false;
        }

        else if(offices.length < 1 ){
            CommonModule.showNotificationError("Please assign at least one Office");
            valid=false;
        }
        else if(roles.length < 1 ){
            CommonModule.showNotificationError("Please assign at least one Role");
            valid=false;
        }


        return valid;
    }
    
    function initializeTemps(model){
        var observable = new kendo.data.ObservableObject(model);
        observable.manager = null;
        var json = observable.toJSON();
        var jsonFilter = JSON.stringify(json);
        
        ServerHandlerModule.callServer("userApi/initTemps/", "post", jsonFilter);
    }

   function onSaveClickCallBack (result) {

       if(result != -1){
           if(editMode)
               CommonModule.showNotificationSuccess("User was updated successfully");
           else
               CommonModule.showNotificationSuccess("User was registered successfully");

           CommonModule.navigationStart("#/user.new");
           CommonModule.navigateTo("#/user.search");
       }
        else{
           CommonModule.showNotificationError("Username already exists");
       }


    }
    

    function configHtmlComponents() {


        $("#tabStripFilter").kendoTabStrip({
            animation:  {
                open: {
                    effects: "fadeIn"
                }
            }
        });

        AutoCompleteModule.autoCompleteBasicConfig($("#manager"), "fullName",
            '/backend/get/autocomplete/manager', function(selectedItem,
                                                              viewModel) {
                var filterObj = viewModel.get('modelView');
                filterObj.managerId = selectedItem.id;

            }, function(event, viewModel) {
                var filterObj = viewModel.get('modelView');
                filterObj.managerId = null;

            });


        var dropbox=$("#status").kendoDropDownList({
            dataSource: DropdownBox.getEnumList('userStatusEnum'), 
            valuePrimitive: true,
            dataTextField: "name",
            dataValueField: "code",
        }).data("kendoDropDownList");

        dropbox.list.addClass('k-list-all');

        $("#office").kendoDropDownList({
            dataSource:offices,
            valuePrimitive: true,
            dataTextField: "name",
            dataValueField: "id",
            optionLabel: {
                name: "Select",
                id: null
            },
            select :function(e){
                $("#office").parent().children().removeClass("errorCombo");
            }
        });

        $("#role").kendoDropDownList({
            dataSource:roles,
            valuePrimitive: true,
            dataTextField: "roleName",
            dataValueField: "roleId",
            optionLabel: {
                roleName: "Select",
                roleId: null
            },
            select :function(e){
                $("#role").parent().children().removeClass("errorCombo");
            }
        });

        $("#officesTable").kendoGrid({
            columns: [

                {
                    title : "",
                    width: 25,
                    template : '<div class="text-center"><a href="javascript:void(0)" class="link action-updateable" onclick="UserModule.removeOffice(#=id#);"><span class="ic-delete lead"></span></a></div>',
                    sortable : false,
                },
                {
                    field: "name",
                    title: "Office",
                    width: 150,
                    sortable: false,

                },
                {
                    field: "state",
                    title: "State",
                    width: 250,
                    sortable: false,

                }, {
                    field: "city",
                    sortable: false,
                    width: 250,
                    title: "City"
                }
            ],
            pageable: true,
            scrollable: false,
            pageable: {
                pageSizes: true,
                previousNext: true,
            }


        });

        $("#rolesTable").kendoGrid({
            columns: [

                {
                    title : "",
                    width: 25,
                    template : '<div class="text-center"><a href="javascript:void(0)" class="link action-updateable" onclick="UserModule.removeRole(#=roleId#);"><span class="ic-delete lead"></span></a></div>',
                    sortable : false,
                },
                {
                    field: "roleName",
                    title: "Name",
                    width: 400,
                    sortable: false,
                }
            ],
            pageable: true,
            scrollable: false,
            pageable: {
                pageSizes: true,
                previousNext: true,
            }
        });


    }

    function removeOffice(officeId) {
        ConfirmModule.showConfirmationDialog("You are about to delete the selected row. Are you sure?",function () {
            var jsonFilter = JSON.stringify(officeId);
            ServerHandlerModule.callServer("/userApi/removeOffice","post",jsonFilter,function(){
                GridModule.dataTableDsConfig($("#officesTable"),dataTableOfficeFields,'/userApi/searchOffices',[],false);
            });

        } );
    }

    function removeRole(roleId) {
        ConfirmModule.showConfirmationDialog("You are about to delete the selected row. Are you sure?",function () {
            var jsonFilter = JSON.stringify(roleId);
            ServerHandlerModule.callServer("/userApi/removeRole","post",jsonFilter,function(){
                GridModule.dataTableDsConfig($("#rolesTable"),dataTableRoleFields,'/userApi/searchRoles',[],false);
            });

        } );
    }

    // API
    return {
        init:init,
        removeOffice:removeOffice,
        removeRole:removeRole,
    };
})();