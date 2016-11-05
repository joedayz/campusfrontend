var RoleModule = (function () {
    var roleNewModel;
    var detailRowActive;

    var gridMarket;

    var isEditing = false;
    var isCreating = false;

    var forceReadOnly ;
    

    var dataRamp ;
    var detailRow ;


    var MarketFilter = kendo.Class.extend({

        roleId: null,
 
    });


    var RampFilter = kendo.Class.extend({
        marketId:null,
        
    });
    
    
    var RoleNew = kendo.Class.extend({
        roleId: null,
        roleName: null,
        roleStatus: null,
        


    });

    var roleNewObj = new RoleNew();


    function initView(value,readCode) {
        initViewRole(value,readCode)
        configHtmlComponents(value,readCode);
        CommonModule.formattHtmlComponents();
    }

    function initViewRole(value,readCode) {

        forceReadOnly=value.isEditForceReadOnly;

        roleNewModel = kendo.observable({
            isEditCode: readCode ,
            modelView: value,
            isVisible : true,
			isEnabled : true,
            onSaveRoleClick: function () {
                var form = $("#createRoleForm");

                form.validate(
                    {
                        errorPlacement: function (error, element) {

                            if(element[0].id=="roleStatus"){$(element).parent().children().addClass("errorCombo");}
                            else{
                                $(element).addClass("errorCombo");
                            }
                            CommonModule.showNotificationError("Please fill in all the required fields");
                        }
                    }
                ).settings.ignore = ":disabled";
                if(!form.valid()){
                    return false;
                }
                console.log("event :: onSaveClick - start");
                var filterObj=this.get('modelView');


                var observable = new kendo.data.ObservableObject(filterObj);

                observable.modules=  $("#moduleResultTable").data("kendoGrid").dataSource.data();

                if (observable.roleStatus instanceof Object)
                    observable.roleStatus = observable.roleStatus.code;


                var json = observable.toJSON();
                var jsonFilter=JSON.stringify(json);


                ServerHandlerModule.callServer("/roleApi/create","post",jsonFilter,onSaveClickCallBack);
                console.log("event :: onSaveClick - Finish");
            },

            onBackClick: function() {
                CommonModule.navigationBack();
            }
        });
        kendo.bind($("#viewRoleForm"), roleNewModel);
    }

    function onSaveClickCallBack(result){

        CommonModule.showNotificationSuccess("Success");
        CommonModule.navigationBack();


    }

    function configHtmlComponents(value,readCode) {
        $("#statusRole").kendoDropDownList({
            dataSource:DropdownBox.getEnumList('activeInactiveStatusEnum'),
            dataTextField: "name",
            dataValueField: "code"

        });

        if(readCode){
            $("#code").addClass("readOnlyInput");

        }
        
        
            var data=value.modules;

            $("#moduleResultTable").kendoGrid({
                indexModules:-1,
                dataSource: {
                    transport: {
                        read: function (o) {
                            console.log("master read");

                            if(typeof value.modules == "undefined" || value.modules==null) {
                                indexModules = -1;

                            }else{
                                if(value.modules.length==0){
                                    indexModules = -1;
                                }else {
                                    indexModules = value.modules.length*-1;
                                    o.success(data);
                                }
                             }

                        },
                        update: function (o) {
                            console.log("master update");

                            o.success();

                        },
                        destroy: function (o) {
                            console.log("master destroy");
                            o.success();
                        },
                        create: function (o) {
                            console.log("master create");
                            var record = o.data;

                            record.id = indexModules;
                            record.marketId=indexModules;
                            indexModules=(indexModules-1);

                                o.success(record);

                        }
                    },
                    schema: {
                        model: {
                            id: "moduleId",
                            fields: {
                                moduleId: { type: "number" , editable: false  },
                                name: { editable: false, type: "string" },
                                permissionType: { editable: true }
                            }
                        }
                    }
                },
                editable:true,
                scrollable : true,
                columns: [
                    { field: "name", title:"Module", template: editor
                    },
                    { field: "permissionType", title: "Permission Type",editor: accesorialDropDownEditor, template: "#=(permissionType.name==null)?'':permissionType.name#" }

                ],
                selectable: false,
                height: 430


            });


            gridMarket = $("#moduleResultTable").data("kendoGrid");
            var expanded = $.map(gridMarket.tbody.children(":has(> .k-hierarchy-cell .k-minus)"), function (row) {
                return $(row).data("uid");
            });

            gridMarket.one("dataBound", function () {

                gridMarket.expandRow(gridMarket.tbody.children().filter(function (idx, row) {
                    return $.inArray($(row).data("uid"), expanded) >= 0;
                }));


            });

        console.log("init configHtmlComponents" );

    }


    function editor(e) {

        if(e.moduleId>=0)
        {
            var  name = e.name;
            return   '<span style="margin-left: 20px!important;">'+name+'</span>';
        }else{
            return  e.name;
        }
    }

    function accesorialDropDownEditor(container, options) {



        if(options.model.moduleId>=0)
        {
 
            var dropDownList = $('<input  name="' + options.field + '" data-text-field="name" class=" combo k-input" data-value-field="code" data-bind="value:' + options.field + '"/>')
                .appendTo(container)
                .kendoDropDownList({
                    autoBind: true,
                    dataSource: DropdownBox.getEnumList('permissionTypeEnum'),
                    //valuePrimitive: true,
                    select: function (e) {
                        var dataItem = this.dataItem(e.item);
                       // options.model.permissionType = dataItem.dataItem;
                        options.model.set("permissionType", dataItem.charge);
                    }
                });


            if(forceReadOnly ==='Y')
                dropDownList.prop('readonly', true).prop('disabled', true);

        } 


    }

    function getStatusMarket() {

          return DropdownBox.getEnumList('activeInactiveStatusEnum');
    }

    function roleNew() {

        CommonModule.navigationStart("#/role.search");
        CommonModule.navigateTo("#/role.new");
    }

    //  API
    return {
        ///Search
        getStatusMarket:getStatusMarket,
        initView: initView,
        roleNew:roleNew

    };
})();

