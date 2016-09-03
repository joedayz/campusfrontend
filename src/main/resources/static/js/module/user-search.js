var userSearchModule = (function () {

    var searchViewModel;
    var filterObj;

    var Filter = kendo.Class.extend({
        userName:'',
        firstName:'',
        lastName:'',
        officeId:null,
        skip: 0,
        take: 10,
        pageSize: 10,
        page: 1

    });



    var dataTableDsFields={
        name: { type: "string" },
        firstName: { type: "string" },
        lastName: { type: "string" },
        offices: { type: "string" },
        status: { type: "string" },
    }

    function init() {
        initView();
        configHtmlComponents();
        CommonModule.formattHtmlComponents();

    }


    function initView(){

        filterObj= new Filter();
        searchViewModel = kendo.observable({
            filterCriteria:filterObj,
            isVisible : true,
			isEnabled : true,
            onResetClick: function() {
            	$("#totalRows").text("0");
                CommonModule.navigateTo("#/user.search");
            //	filterObj = new VendorFilter();
            //	this.set("filterCriteria", filterObj);
             //   $("#userResultTable").data('kendoGrid').dataSource.data([]);
            },
            onSearchClick: function() {
                CommonModule.cleanNotification();
                filterObj=this.get('filterCriteria');
                GridModule.dataTableDsConfig($("#userResultTable"),dataTableDsFields,
                		'/userApi/search',filterObj,PAGING_ON_SERVER, $("#totalRows"));
            },
            onAddNewClick: function () {
                CommonModule.navigationStart("#/user.search");
                CommonModule.navigateTo("#/user.new");
            }
        });


        kendo.bind($("#criteria"), searchViewModel);
    }

    function configHtmlComponents () {

        $("#office").kendoDropDownList({
            dataSource:offices,
            valuePrimitive: true,
            dataTextField: "name",
            dataValueField: "id",
            optionLabel: {
                name: "ALL",
                id: null
            }

        });

        $("#totalRows").text("0");
        
        $("#userResultTable").kendoGrid({
            columns: [
                {
                    title : "",
                    width: 40,
                    template : '<div class="text-center"><a href="javascript:void(0)" class="link" onclick="userSearchModule.userEdit(#=id#);" ><span class="ic-edit lead"></span></a></div>',
                    sortable : false,
                },

                {
                    title : "",
                    width: 40,
                    template : '<div class="text-center"><a href="javascript:void(0)" class="link action-updateable" onclick="userSearchModule.userDelete(#=id#);"><span class="ic-delete lead"></span></a></div>',
                    sortable : false,
                },
                {
                    field: "name",
                    title: "Username",
                    width: "20%"

                }, {
                    field: "firstName",
                    width: "20%",
                    title: "First Name"
                }, {
                    field: "lastName",
                    width: "25%",
                    title: "Last Name"
                }, {
                    field: "offices",
                    width: "25%",
                    title: "Office"
                }, {
                    field: "status",
                    width: "10%",
                    title: "Status"
                }

            ],
            pageable: true,
            sortable: true,

            pageable: {
                pageSizes: true,
                buttonCount: 5,


            },

        });


    }

    function userEdit(userId){
        CommonModule.navigationStart("#/user.search");
        CommonModule.navigateTo("#/user.edit?userId="+userId);
    }
    function userDelete(userId){
        ConfirmModule.showConfirmationDialog("You are about to delete the selected row. Are you sure?",function(){
            ServerHandlerModule.callServer(
                        "/backend/post/user/delete/", "post",
                        kendo.stringify({
                            id : userId
                        }),deleteCallBack);
            }
            );
    }

    function deleteCallBack(result){
        if(result.message.valueOf()=="success"){
            filterObj=searchViewModel.get('filterCriteria');
            GridModule.dataTableDsConfig($("#userResultTable"),dataTableDsFields,'/userApi/search',filterObj);
        }else
            CommonModule.showNotificationError(result.message.concat(". Can't be deleted"));
    }

    //  API
    return {
        init: init,
        userDelete:userDelete,
        userEdit:userEdit,
    };
})();
