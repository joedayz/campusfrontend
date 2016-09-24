var ModuleSearchModule = (function() {

	var ModuleFilter = kendo.Class.extend({

		code : null,
		name : null,
		parentName : null,
		rowVersion: '0'
	});

	var ModuleFilterParams = kendo.Class.extend({
		code : null,
		name : null,
		parentName : null,
		skip : 0,
		take : 10,
		pageSize : 10,
		page : 1,

	});

	var dataTableDsFields = {
		code : {
			type : "string"
		},
		name : {
			type : "string"
		},
		menuLabel : {
			type : "string"
		},
		menuOrder : {
			type : "string"
		},
		visible : {
			type : "string"
		},
		parentName : {
			type : "string"
		}

	}

	var filterObj = new ModuleFilter();

	function initModuleSearch() {
		initModuleSearchView();
		configHtmlComponents();
		CommonModule.formattHtmlComponents();
	}

	function initModuleSearchView() {

		var moduleSearchViewModel = kendo.observable({

			filterCriteria : filterObj,

			isVisible : true,
			isEnabled : true,
			onCleanClick : function() {
				filterObj = new ModuleFilter();
				this.set("filterCriteria", filterObj);
				$("#totalRows").text("0");
				$("#moduleResultTable").data('kendoGrid').dataSource.data([]);

			},
			onSearchClick : function() {

				filterObj = this.get('filterCriteria');

				var params = new ModuleFilterParams();

				var paramValues = _.extend(params, filterObj);

				GridModule.dataTableDsConfig($("#moduleResultTable"),
						dataTableDsFields, '/module/search/find', paramValues, PAGING_ON_SERVER, $("#totalRows"));

			}
		});

		kendo.bind($("#searchForm"), moduleSearchViewModel);
	}

	function configHtmlComponents() {

		$("#totalRows").text("0");
		
		$("#moduleResultTable")
				.kendoGrid(
						{
							columns : [

									{
										title : "",
										template : '<div class="text-center"><a href="javascript:void(0)" class="link" onclick="ModuleSearchModule.moduleView(#=moduleId#);" ><span class="ic-edit lead"></span></a></div>',
										sortable : false,
										width: 40
									}, {
										field : "code",
										title : "Code",
										width: "10%"
									}, {
										field : "name",
										title : "Name",
										width: "24%"
									}, {
										field : "menuLabel",
										title : "Menu Label",
										width: "23%"
									}, {
										field : "menuOrder",
										title : "Menu Order",
										width: "10%"
									}, {
										field : "visible",
										title : "Visible",
										width: "8%"
									}, {
										field : "parentName",
										title : "Parent",
										width: "25%"
									} ],
							pageable : true,
							pageable : {
								pageSizes : true,
								buttonCount : 5,
							},

						});
	}

	function moduleView(moduleId) {
		CommonModule.navigateTo("#/module.view?moduleId=" + moduleId);
		
		filterObj = new ModuleFilter();
		
	}
	
	// API
	return {
		// /Search
		initModuleSearch : initModuleSearch,
		moduleView : moduleView
	};
})();
