var GeneralTableSearchModule = (function() {

	var GeneralTableFilter = kendo.Class.extend({

		tableName : "",
		code : "",
		rowVersion: '0'

	});
	
	var GeneralTableFilterParams = kendo.Class.extend({

		tableName : "",
		code : "",
		skip : 0,
		take : 10,
		pageSize : 10,
		page : 1,
		rowVersion: '0'

	});
	
	var dataTableDsFields = {
		tableName : {
			type : "string"
		},
		code : {
			type : "string"
		},
		value : {
			type : "string"
		},
		sortOrder : {
			type : "string"
		},
		status : {
			type : "string"
		}
	}


	var filterObj = new GeneralTableFilter();

	function initGeneralTableSearch() {
		initGeneralTableSearchView();
		configHtmlComponents();
		CommonModule.formattHtmlComponents();
	}

	function initGeneralTableSearchView() {

		var filterObj = new GeneralTableFilter();

		var generalTableSearchViewModel = kendo.observable({
			filterCriteria : filterObj,
			isVisible : true,
			isEnabled : true,
			onCleanClick : function() {
				
				filterObj = new GeneralTableFilter();
				this.set("filterCriteria", filterObj);
				$("#totalRows").text("0");
				$("#generalTableResultTable").data('kendoGrid').dataSource.data([]);
				
			},
			onSearchClick : function() {

				filterObj = this.get('filterCriteria');
				
				var params = new GeneralTableFilterParams();
				
				var paramValues = _.extend(params, filterObj);
				
				GridModule.dataTableDsConfig($("#generalTableResultTable"), dataTableDsFields, 
						'/generalTable/search/find', paramValues,
						 PAGING_ON_SERVER, $("#totalRows"));
			},
			onRedirectNewClick : function() {
				CommonModule.navigationStart("#/generalTable.search");
				CommonModule.navigateTo("#/generalTable.new");
				GeneralTableModule.newModel();
				
				filterObj = new GeneralTableFilter();
				this.set("filterCriteria", filterObj);
			}
		});

		kendo.bind($("#searchForm"), generalTableSearchViewModel);
	}

	function configHtmlComponents() {

		var dropbox = $("#tableName").kendoDropDownList({
			dataSource : generalTables,
			dataTextField : "code",
			dataValueField : "value",
			optionLabel : {
				code : "ALL",
				value : ""
			}
		}).data("kendoDropDownList");
		dropbox.list.addClass('k-list-all');
		
		$("#totalRows").text("0");

		$("#generalTableResultTable").kendoGrid({
							columns : [
							        {
										title : "",
										template : '<div class="text-center"><a href="javascript:void(0)" class="link" onclick="GeneralTableSearchModule.generalTableView(#=generalTableId#);" ><span class="ic-edit lead"></span></a></div>',
										sortable : false,
										width : 40,
									},
//									{
//										title : "",
//										template : '<div class="text-center"><a href="javascript:void(0)" class="link action-updateable" onclick="GeneralTableSearchModule.generalTableDelete(#=generalTableId#);"><span class="ic-delete lead"></span></a></div>',
//										sortable : false,
//										width : "5%"
//									}, 
									{
										field : "tableName",
										title : "Group",
										width : "40%"
									}, {
										field : "code",
										title : "Code",
										width : "20%"
									}, {
										field : "value",
										title : "Value",
										width : "20%"
									}, {
										field : "sortOrder",
										title : "Order",
										width : "10"
									}, {
										field : "status",
										title : "Status",
										width : "10%"
									} ],
							pageable : true,
							pageable : {
								pageSizes : true,
								buttonCount : 5,

							},

						});
	}

	function generalTableDelete(generalTableId) {

		ConfirmModule.showConfirmationDialog("You are about to delete the selected row. Are you sure?",function(){
			ServerHandlerModule
				.callServer(
					"/backend/post/generalTable/delete/",
					"post",
					kendo.stringify({
						generalTableId : generalTableId
					}),
					function(result) {
						$('#generalTableResultTable').data('kendoGrid').dataSource.read();
						$('#generalTableResultTable').data('kendoGrid').refresh();
						CommonModule.showNotificationSuccess(result.message);
					});
		}
		,function () {
			//Cancel
		});
		
	}



	function generalTableView(generalTableId) {
		CommonModule.navigateTo("#/generalTable.view?generalTableId=" + generalTableId);
		filterObj = new GeneralTableFilter();

	}

	// API
	return {
		// /Search
		initGeneralTableSearch : initGeneralTableSearch,
		generalTableView : generalTableView,
		generalTableDelete : generalTableDelete,

	};
})();
