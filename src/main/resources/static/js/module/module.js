var ModulesModule = (function() {
	var moduleViewModel;

	var Module = kendo.Class.extend({
		moduleId : null,
		code : "",
		name : "",
		menuLabel : "",
		menuOrder : "",
		url : "",
		helpUrl : "",
		visible : "",
		parentModuleId : null,
		rowVersion : "0"
	});

	var moduleNew = new Module();

	function newModel() {
		return new Module();
	}

	function initModule(value) {

		configHtmlComponents(value);
		CommonModule.formattHtmlComponents();
		initModuleEdit(value);
	}

	function initModuleEdit(value) {

		var moduleNew = new Module();
		moduleNew = _.extend(moduleNew, value);

		if (moduleNew.moduleId == null) {
			moduleNew = new Module();
			$("#code").prop("disabled", false);
			$("#url").prop("disabled", false);
			$("#helpUrl").prop("disabled", false);
		} else {
			$("#code").prop("disabled", true);
			$("#url").prop("disabled", true);
			$("#helpUrl").prop("disabled", true);
		}

		moduleViewModel = kendo.observable({
				modelView : moduleNew,
				isVisible : true,
				isEnabled : true,
				onCancelClick : function() {
					CommonModule.navigateTo("#/module.search");
				},
				onInsertClick : function() {

					var form = $("#moduleForm");
					form.validate({
						errorPlacement : function(error, element) {

							if (element[0].id == "name") {
								$(element).parent().children().addClass("errorCombo");
							}else if(element[0].id=="menuLabel"){ 
								$(element).parent().children().addClass("errorCombo");
							}else if(element[0].id=="menuOrder"){ 
								$(element).parent().children().addClass("errorCombo");
							}

							else {
								$(element).addClass("errorCombo");
							}
						}
					}).settings.ignore = ":disabled";
					if (!form.valid()) {
						CommonModule.showNotificationError("Please fill in all the required fields");
						return false;
					}

					filterObj = this.get('modelView');
					
					var observable = new kendo.data.ObservableObject(filterObj);

					if(observable.visible!=null && typeof (observable.visible)==='boolean')
	                    observable.visible=observable.visible?"Y":"N";
					
					var json = observable.toJSON();
					var jsonFilter = JSON.stringify(json);
					
					ServerHandlerModule.callServer("/backend/post/module/update/", "post", jsonFilter, onSaveClickCallBack);

				}
			});

		kendo.bind($("#criteria"), moduleViewModel);

	}

	function onSaveClickCallBack(result) {
		CommonModule.showNotificationSuccess("Success");
		CommonModule.navigateTo("#/module.search");
	}

	function validateInformation() {
		var model = moduleViewModel.get('modelView');

		if (model.name == '') {
			CommonModule.showNotificationError("Code not valid");

			return false;
		}
		if (model.menuLabel == '') {
			CommonModule.showNotificationError("Menu Label not valid");

			return false;
		}
		
		if (model.menuOrder == '') {
			CommonModule.showNotificationError("Menu Order not valid");

			return false;
		}
		
		return true;
	}

	function configHtmlComponents(value) {

		var dropbox = $("#parentModuleId").kendoDropDownList({
			dataSource : modules,
			dataTextField : "name",
			dataValueField : "moduleId",
			optionLabel : {
				name : "SELECT",
				moduleId : 0
			}
		}).data("kendoDropDownList");
		dropbox.list.addClass('k-list-all');
		
		$("#name").keyup(function(){
			$("#name").parent().children().removeClass("errorCombo");
		});
		
		$("#menuLabel").keyup(function(){
			$("#menuLabel").parent().children().removeClass("errorCombo");
		});
		
		$("#menuOrder").keyup(function(){
			$("#menuOrder").parent().children().removeClass("errorCombo");
		});
		
	}

	return {

		initModule : initModule,
		newModel : newModel,

	};
})();