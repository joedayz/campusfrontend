var ParameterSearchModule = (function() {

	var ParameterFilter = kendo.Class.extend({

		code : null,
		description : null

	});

	var ParameterFilterParams = kendo.Class.extend({
		code : null,
		description : null,
		skip : 0,
		take : 10,
		pageSize : 10,
		page : 1,
		rowVersion : '0'

	});

	var dataTableDsFields = {
		code : {
			type : "string"
		},
		description : {
			type : "string"
		},
		value : {
			type : "string"
		}		

	}

	var filterObj = new ParameterFilter();

	function initParameterSearch() {
		initParameterSearchView();
		configHtmlComponents();
		CommonModule.formattHtmlComponents();
	}

	function initParameterSearchView() {

		var parameterSearchViewModel = kendo.observable({
			filterCriteria : filterObj,
			isVisible : true,
			isEnabled : true,
			onCleanClick : function() {
				filterObj = new ParameterFilter();
				this.set("filterCriteria", filterObj);
				$("#totalRows").text("0");
				$("#parameterResultTable").data('kendoGrid').dataSource.data([]);
			},
			onSearchClick : function() {

				filterObj = this.get('filterCriteria');

				var params = new ParameterFilterParams();

				var paramValues = _.extend(params, filterObj);

				GridModule.dataTableDsConfig($("#parameterResultTable"), dataTableDsFields, 
						'/parameter/search/find', paramValues, PAGING_ON_SERVER, $("#totalRows"));

			},
			onRedirectNewClick : function() {
								
				CommonModule.navigationStart("#/parameter.search");
			    CommonModule.navigateTo("#/parameter.new");	
			    ParameterModule.newModel();
			    
			    filterObj = new ParameterFilter();
				this.set("filterCriteria", filterObj);
			    
			}
		});

		kendo.bind($("#searchForm"), parameterSearchViewModel);
	}

	function configHtmlComponents() {

		$("#totalRows").text("0");
		
		$("#parameterResultTable").kendoGrid(
						{
							columns : [

									{
										title : "",
										template : '<div class="text-center"><a href="javascript:void(0)" class="link" onclick="ParameterSearchModule.parameterView(#=parameterId#);" ><span class="ic-edit lead"></span></a></div>',
										sortable : false,
										width: 40
									}, {
										field : "code",
										title : "Code",
										width: "30%"
									}, {
										field : "description",
										title : "Description",
										width: "50%"
									}, {
										field : "value",
										title : "Value",
										width: "20%"
									}
									],
							pageable : true,
							pageable : {
								pageSizes : true,
								buttonCount : 5,

							},

						});
	}

	function parameterView(parameterId) {
		CommonModule.navigateTo("#/parameter.view?parameterId=" + parameterId);
		
		filterObj = new ParameterFilter();
		
	}

	// API
	return {
		// /Search
		initParameterSearch : initParameterSearch,
		parameterView : parameterView,

	};
})();
