var GeneralTableModule = (function() {
	var generalTableNewModel;

	var GeneralTable = kendo.Class.extend({
		generalTableId : null,
		tableName : "",
		code : "",
		value : "",
		sortOrder : null,
		status : "A",
		rowVersion : "0"
	});

	var generalTableNew = new GeneralTable();
	
	function newModel() {
		return new GeneralTable();
	}

	function initGeneralTable(value) {
		configHtmlComponents(value);
		CommonModule.formattHtmlComponents();
		initGeneralTableEdit(value);

	}
	
	function initGeneralTableEdit(value) {

		var generalTableNew = new GeneralTable();
		generalTableNew = _.extend(generalTableNew, value);
		
		if (generalTableNew.generalTableId == null) {
			generalTableNew = new GeneralTable();
			$("#code").prop("disabled", false);
		} else {
			$("#code").prop("disabled", true);
			$("#tableName").data("kendoDropDownList").readonly();
		}

		generalTableViewModel = kendo.observable({
					modelView : generalTableNew,
					isVisible : true,
					isEnabled : true,
					onCancelClick : function() {
						CommonModule.navigateTo("#/generalTable.search");
					},
					onInsertClick : function() {

						var form = $("#generalTableForm");
						form.validate({
									errorPlacement : function(error, element) {

										if (element[0].id == "value") {
											$(element).parent().children().addClass("errorCombo");
										}else if (element[0].id == "sortOrder") {
											$(element).parent().children().addClass("errorCombo");
										}else if (element[0].id == "code") {
											$(element).parent().children().addClass("errorCombo");
										}else if (element[0].id == "tableName") {
											$(element).parent().children().addClass("errorCombo");
										}else{
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

						var json = observable.toJSON();
						var jsonFilter = JSON.stringify(json);

						ServerHandlerModule.callServer("/backend/post/generalTable/create/","post", jsonFilter, onSaveClickCallBack);

					}
				});

		kendo.bind($("#criteria"), generalTableViewModel);
	}

	function onSaveClickCallBack(result) {
		CommonModule.showNotificationSuccess("Success");
		CommonModule.navigateTo("#/generalTable.search");
	}
	
	function validateInformation() {
		var model = parameterViewModel.get('modelView');

		if (model.value == '') {
			CommonModule.showNotificationError("Value not valid");

			return false;
		}
		if (model.sortOrder == '') {
			CommonModule.showNotificationError("Sort Order not valid");

			return false;
		}
		
		if (model.tableName == '') {
			CommonModule.showNotificationError("Group not valid");

			return false;
		}
		if (model.code == '') {
			CommonModule.showNotificationError("Code not valid");

			return false;
		}

		return true;
	}

	function configHtmlComponents(value) {

		var dropbox = $("#tableName").kendoDropDownList({
			dataSource : generalTables,
			dataTextField : "code",
			dataValueField : "value",
			optionLabel : {
				code : "SELECT",
				value : ""
			},change : function(e) {
				$("#tableName").parent().children().removeClass("errorCombo");
			}
		}).data("kendoDropDownList");
		dropbox.list.addClass('k-list-all');

		var dropbox = $("#status").kendoDropDownList({
			dataSource : DropdownBox.getEnumList('activeInactiveStatusEnum'),
			dataTextField : "name",
			dataValueField : "code",
			optionLabel : {
				name : "SELECT",
				code : ""
			}

		}).data("kendoDropDownList");
		dropbox.list.addClass('k-list-all');

		$("#code").keyup(function(){
			$("#code").parent().children().removeClass("errorCombo");
		});
		
		$("#value").keyup(function(){
			$("#value").parent().children().removeClass("errorCombo");
		});
		
		$("#sortOrder").keyup(function(){
			$("#sortOrder").parent().children().removeClass("errorCombo");
		});
		
		
	}

	return {

		initGeneralTable : initGeneralTable,
		newModel : newModel,
	};
})();
