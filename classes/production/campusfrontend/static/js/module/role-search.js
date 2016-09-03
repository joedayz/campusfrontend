var RoleSearchModule = (function () {



    var roleSearchViewModel;

    var RoleFilter = kendo.Class.extend({

        roleName: null,
        
    });




 
    function initRoleSearch() {
        initSearchView();
        configHtmlComponents();
        CommonModule.formattHtmlComponents();

    }


    function initSearchView() {

        var filterObj = new RoleFilter();

         roleSearchViewModel = kendo.observable({
            filterRoleCriteria: filterObj,
            isVisible : true,
			isEnabled : true,
            onCleanClick: function () {
                CommonModule.cleanNotification();
                filterObj = new RoleFilter();

                this.set("filterRoleCriteria", filterObj);
                $("#totalRows").text("0");
                $("#roleResultTable").data('kendoGrid').dataSource.data([]);

            },
            onSearchClick: function () {

                CommonModule.cleanNotification();

                filterObj = this.get('filterRoleCriteria');

                if(filterObj.name instanceof Object) {
                    filterObj.name=filterObj.name.marketName;
                }





                setDataSourceInGridRole(filterObj);

            },
            onAddRoleClick: function () {
                RoleModule.roleNew();
            },

        });


        kendo.bind($("#criteriaRole"), roleSearchViewModel);
    }



    function configHtmlComponents(rfqModel) {
       // '/backend/get/autocomplete/market', function(selectedItem, viewModel) {
        AutoCompleteModule.autoCompleteConfig($("#name"), 'marketName',
            '/marketApi/find/autocomplete', function(selectedItem, viewModel) {

                var filterObj = viewModel.get('filterRoleCriteria');
                filterObj.marketId = selectedItem.marketId;
                filterObj.marketNameAfter=selectedItem.marketName;
                filterObj.name=selectedItem.marketName;
                console.log("select event handler");


            }, function(event, viewModel) {

                var filterObj = viewModel.get('filterRoleCriteria');
                filterObj.marketId = null;

                $("#name").data("kendoAutoComplete").value("");

            },null);

        $("#totalRows").text("0");

            $("#roleResultTable").kendoGrid({
                columns: [
                    {

                    field: "roleId",
                    title: "          ",
                    width: 40,
                    template:  editor     ,
                    sortable: false,
                    attributes: {
                        style: "text-align: right;"
                    }
                    },
                    
                    {

                        field: "roleId",
                        title: "          ",
                        width: 40,
                        template:  editor2     ,
                        sortable: false,
                        attributes: {
                            style: "text-align: right;"
                        }
                        }
                    ,
                    
                    {
                        field: "roleCode",
                        title: "Code",
                        sortable: false,
                        width: "25%"
                    },
                    {
                        field: "roleName",
                        title: "Name",
                        sortable: false,
                        width: "60%"
                    }, {
                        field: "roleStatus",
                        title: "Status",
                        sortable: false,
                        width: "15%"
                    }

                ],
                pageable: true,
                sortable: false,
                selectable: "row",
                pageable: {
                    pageSizes: true,
                    buttonCount: 5,


                },

            });

    }

    function editor(e) {
        var accessEditor='';


        if(e.isUpdateRole=='Y')
        {
            accessEditor+= '<div class="text-center"><a href="javascript:void(0)" class="link" onclick="RoleSearchModule.roleView('+e.roleId+');" ><span class="ic-edit lead"></span></a></div>';
        }else{
            accessEditor+='';
        }

//        if(e.isDeleteRole=='Y')
//        {
//            accessEditor+='<div class="text-center"><a href="javascript:void(0)" class="link action-updateable" onclick="RoleSearchModule.roleDelete('+e.roleId+');"><span class="ic-delete lead"></span></a></div>';
//        }else{
//            accessEditor+='';
//        }
 
        return accessEditor;

    }
    
    function editor2(e) {
        var accessDel='';


        if(e.isDeleteRole=='Y')
        {
        	accessDel+='<div class="text-center"><a href="javascript:void(0)" class="link action-updateable" onclick="RoleSearchModule.roleDelete('+e.roleId+');"><span class="ic-delete lead"></span></a></div>';
        }else{
        	accessDel+='';
        }
 
        return accessDel;

    }


    function roleView(roleId){
 
        CommonModule.navigationStart("#/role.search");
        CommonModule.navigateTo("#/role.edit?roleId="+roleId);

    }


    function roleDelete(roleId){

        ConfirmModule.showConfirmationDialog("You are about to delete the selected row. Are you sure?",function(){
            var obj={roleId:roleId};

            ServerHandlerModule.callServer("/roleApi/delete","post",kendo.stringify(obj),onDeleteRoleClickCallBack);

        },function () {
        });

    }




    function onDeleteRoleClickCallBack(result){
        CommonModule.showNotificationSuccess(result.message);

        var viewModel = $("#roleName").get(0).kendoBindingTarget.source;
        var filterObj = viewModel.get('filterRoleCriteria');

        setDataSourceInGridRole(filterObj);

    }


    function setDataSourceInGridRole(filterObj) {

        var dataSourceGrid = new kendo.data.DataSource({
            type: "application/json",
            serverSorting: true,
            serverPaging: true,
            serverFiltering: true,
            schema: {
                total: "total",
                data: "resultList",
                model: {
                    fields: {
                        roleId: {type: "number"},
                        roleName: {type: "string"},
                        roleStatus: {type: "string"},
                        markets: {type: "number"}
                    }
                }
            }
            ,
            transport: {
                read: function (options) {

                    var params = new RoleFilter();
                    var paramValues = _.extend(params, filterObj);

                //    if(filterObj.marketId == null){  $("#name").data("kendoAutoComplete").value("");};

                    CommonModule.gridReadActionAjax("/roleApi/search/find", options, paramValues);
                },


            },change: function(e) {
                
            	$("#totalRows").text(this.total());                	
                
            }
            ,
            pageSize: 10
        });


        var grid = $("#roleResultTable").data("kendoGrid");

        if(grid)
            grid.setDataSource(dataSourceGrid);
        
        console.log("event :: click : Refreshing grid");

    }




    //  API
    return {
        ///Search
        initRoleSearch: initRoleSearch,
        roleView:roleView,
        roleDelete:roleDelete

    };
})();
