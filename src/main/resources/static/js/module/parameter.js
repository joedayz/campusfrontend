var ParameterModule = (function() {
	var parameterViewModel;

	var Parameter = kendo.Class.extend({
		parameterId : null,
		code : "",
		description : "",
		value : "",
		status : "A",
		rowVersion: "0"
	});

	var parameterNew = new Parameter();

	function newModel() {
		return new Parameter();
	}

	function initParameter(value) {

		configHtmlComponents(value);
		CommonModule.formattHtmlComponents();
		initParameterEdit(value);
	}

	function initParameterEdit(value) {

		var parameterNew = new Parameter();
		parameterNew = _.extend(parameterNew, value);

		if (parameterNew.parameterId == null) {
			parameterNew = new Parameter();
			$("#code").prop("disabled", false);
		}else{
			$("#code").prop("disabled", true);
		}

		parameterViewModel = kendo
				.observable({
					modelView : parameterNew,
					isVisible : true,
					isEnabled : true,
					onCancelClick : function() {
						CommonModule.navigateTo("#/parameter.search");
					},
					onInsertClick : function() {
						
						var form = $("#parameterForm");
						form.validate({
							errorPlacement: function (error, element) {

								if(element[0].id=="code"){ 
									$(element).parent().children().addClass("errorCombo");
								}else if(element[0].id=="value"){ 
									$(element).parent().children().addClass("errorCombo");
								}else if(element[0].id=="description"){ 
									$(element).parent().children().addClass("errorCombo");
								}else{
			                        $(element).addClass("errorCombo");
			                    }
			                 }
			            }).settings.ignore = ":disabled";
			            if(!form.valid()){
			            	CommonModule.showNotificationError("Please fill in all the required fields");
			                return false;
			            }
			                
			            filterObj = this.get('modelView');
						var observable = new kendo.data.ObservableObject(filterObj);

							
						var json = observable.toJSON();
						var jsonFilter = JSON.stringify(json);

						ServerHandlerModule.callServer("/backend/post/parameter/create/", "post",jsonFilter, onSaveClickCallBack);
					}
				});

		kendo.bind($("#criteria"), parameterViewModel);

	}

	function onSaveClickCallBack(result) {
		CommonModule.showNotificationSuccess("Success");
		CommonModule.navigateTo("#/parameter.search");
	}

	function validateInformation() {
		var model = parameterViewModel.get('modelView');

		if (model.description == '') {
			CommonModule.showNotificationError("Description not valid");

			return false;
		}
		if (model.value == '') {
			CommonModule.showNotificationError("Value not valid");

			return false;
		}

		return true;
	}

	function configHtmlComponents(value) {

		$("#code").keyup(function(){
			$("#code").parent().children().removeClass("errorCombo");
		});
		
		$("#value").keyup(function(){
			$("#value").parent().children().removeClass("errorCombo");
		});
		
		$("#description").keyup(function(){
			$("#description").parent().children().removeClass("errorCombo");
		});


	}

	return {

		initParameter : initParameter,
		newModel : newModel,

	};
})();
