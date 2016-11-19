(function ($, kendo) {
    var ConfigGrid = kendo.ui.Grid.extend({
        options: {
            toolbarColumnMenu: false,
            gridConfig: [],
            name: "ConfigGrid"
        },

        init: function (element, options) {
            // Call the base class init.
            kendo.ui.Grid.fn.init.call(this, element, options);

            this._initToolbarColumnMenu();
        },

        _initToolbarColumnMenu: function () {

            var that = this;

            //Init the columns
            var gridId = this.table[0].id;
            var currentConfig = {};
            for (var idx in gridConfig){
                var cfg = gridConfig[idx];
                if (cfg.gridName === gridId){
                    currentConfig = cfg;
                }
            }

            this._configCols(currentConfig);

            // Create the column menu items.
            var $menu = $("<ul id='id_dropdown-menu' class='dropdown-menu'></ul>");

            // Loop over all the columns and add them to the column menu.
            for (var idx = 0; idx < this.columns.length; idx++) {
                var column = this.columns[idx];
                // A column must have a title to be added.
                if ($.trim(column.title).length > 0) {
                    // Add columns to the column menu.
                    $menu.append(kendo.format("<li><input  type='checkbox' data-index='{0}' data-field='{1}' data-title='{2}' {3}>{4}</li>",
                        idx, column.field, column.title, column.hidden ? "" : "checked", this._removeBreaklines(column.title)));
                }
            }

            $menu.insertAfter($("button.dropdown-toggle-" + gridId));

            $('#id_dropdown-menu li').click(function(e)
            {
                //alert($(this).find("input").checked());
                var $input = $(this).find("input");

                var column = that._findColumnByTitle($input.attr("data-title"));

                // If checked, then show the column; otherwise hide the column.
                if ($input.is(":checked")) {
                    that._removeFromHiddenCols(currentConfig, column.field)
                    that.showColumn(column);
                } else {
                    var hideColCount = 0;
                    var hiddeableCols = 0;
                    for (var idx = 0; idx < that.columns.length; idx++) {
                        var cl = that.columns[idx];
                        if (cl.title) {
                            hiddeableCols++;
                        }
                    }

                    for (var idx = 0; idx < that.columns.length; idx++) {
                        var cl = that.columns[idx];
                        if (cl.title && cl.hidden) {
                            hideColCount++;
                        }
                    }
                    if (1 === hiddeableCols - hideColCount){
                        CommonModule.showNotificationError("You can not hide the last column");
                        $input.prop('checked', true);
                        return;
                    }

                    that._addToHiddenCols(currentConfig, column.field)
                    that.hideColumn(column);
                }

                if (currentConfig.hiddenColumnKeys){
                    currentConfig.hiddenColumnKeys = currentConfig.hiddenColumnKeys.replace(",,",",");
                }
                ServerHandlerModule.callServer(
                    "/backend/post/gridConfig", "post",
                    JSON.stringify(currentConfig), this._onSaveColumnCallBack);

            });


            // }
        },

        _removeFromHiddenCols: function (gridConfig, field) {
            if (gridConfig.hiddenColumnKeys && gridConfig.hiddenColumnKeys.indexOf(field) > -1){
                var result = "";
                if (gridConfig.hiddenColumnKeys.indexOf(field + ",") > -1){
                    gridConfig.hiddenColumnKeys = gridConfig.hiddenColumnKeys.replace(field + ",", "");
                } else {
                    gridConfig.hiddenColumnKeys = gridConfig.hiddenColumnKeys.replace(field, "");
                }

            } else {
                console.info("Doesnt found field in hidden columns");
            }
        },

        _addToHiddenCols: function (gridConfig, field) {
            if(gridConfig.hiddenColumnKeys){
                gridConfig.hiddenColumnKeys += "," + field
            } else {
                gridConfig.hiddenColumnKeys = field
            }
        },

        _configCols: function (gridConfig) {
            if (!gridConfig.hiddenColumnKeys){
                return;
            }
            var fields = gridConfig.hiddenColumnKeys.split(",");
            for (var idx in fields){
                var fld = fields[idx];
                var column = this._findColumnByField(fld);
                if (column){
                    this.hideColumn(column);
                }
            }
        },

        _onSaveColumnCallBack: function(result) {
            CommonModule.showNotificationSuccess("Saving grid preferences...");
        },


        _removeBreaklines: function (fieldName) {
            /// <summary>
            /// Removes the \n in the field name.
            /// </summary>

            return fieldName.replace("<br/>","");
        },

        _findColumnByTitle: function (title) {
            /// <summary>
            /// Find a column by column title.
            /// </summary>

            var result = null;

            for (var idx = 0; idx < this.columns.length && result === null; idx++) {
                column = this.columns[idx];

                if (column.title === title) {
                    result = column;
                }
            }

            return result;
        },

        _findColumnByField: function (field) {
            /// <summary>
            /// Find a column by column field.
            /// </summary>

            var result = null;

            for (var idx = 0; idx < this.columns.length && result === null; idx++) {
                column = this.columns[idx];

                if (column.field === field) {
                    result = column;
                }
            }

            return result;
        }
    });
    kendo.ui.plugin(ConfigGrid);
})(window.kendo.jQuery, window.kendo);