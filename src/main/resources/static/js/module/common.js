var readOnlyModule=false;

const PAGING_ON_SERVER = true;
const PAGING_ON_CLIENT = false;

if (typeof String.prototype.trim !== 'function') {
    String.prototype.trim = function () {
        return this.replace(/^\s+|\s+$/g, '');
    }
}


if (typeof String.prototype.isBlank !== 'function') {
    String.prototype.isBlank = function () {
        return (!str || /^\s*$/.test(str));
    }
}


$(document)

    .ajaxStart(function () {
        // alert('ajaxStart');
        console.log("ajaxStart Handler");
        //ComponenteUtil.blockBody("Procesando");
        CommonModule.blockProgress();
    })


    .ajaxError(function (e, jqxhr, settings, exception) {

        console.log("ajaxError Handler:"+JSON.stringify(jqxhr));
        if (jqxhr.status === 401) {
            console.info("No authorizado, no se debe pintar");
            return;
        }
        //alert('ajaxError:'+JSON.stringify(jqxhr));
        if (jqxhr.status === 500) {
            CommonModule.showNotificationError(jqxhr.error )
        }

        e.stopPropagation();
        /*  //alert(e);
         e.stopPropagation();
         if (jqxhr != null) {

         // alert(jqxhr.responseText);
         if (jqxhr.status === 401) {
         //HTTP Error 401 Unauthorized.
         //alert("ajaxError: " + jqxhr.responseText);
         if (jqxhr.responseText.indexOf("NavigateTo") > 0) {

         //alert("NavigateTo encontrado");
         var params = $.parseJSON(jqxhr.responseText);
         window.location.href = params.NavigateTo;
         } else {

         //alert("Vamos a AccesoDenegado");
         window.location.href = "/Error/AccesoDenegado";
         }

         }
         if (jqxhr.status === 500) {
         //
         if (jqxhr.responseText.indexOf("ErrorMessage") > 0) {
         var error = $.parseJSON(jqxhr.responseText);
         //ComponenteUtil.unblockBody();
         ComponenteUtil.showMessage(error.ErrorMessage, error.ErrorType);

         } else {
         window.location.href = "/Error/Error";
         }

         }

         }
         */
    })
    .ajaxSuccess(function (e, xhr, opts) {
        //alert('ajaxSuccess:'+JSON.stringify(xhr));
        CommonModule.unBlockProgress();
        console.log("ajaxSuccess Handler:"+JSON.stringify(xhr));


    }).ajaxComplete(function (e, xhr, opts) {
    console.log("ajaxComplete Handler");
    CommonModule.unBlockProgress();
    // alert('ajaxComplete:'+JSON.stringify(xhr));

}).ajaxStop(function () {

});




var CommonModule = (function () {

    function blockProgress(){
        console.log("blockProgress kendo.ui.progress (true)");
        kendo.ui.progress($(".loading"), true);
    }

    function unBlockProgress(){
        console.log("unBlockProgress kendo.ui.progress (false)");
        kendo.ui.progress($(".loading"), false);
    }
    function init(){

    }



    function formattHtmlComponentsDatetime(value){
        $(value).each(function() {
            var input=$(this);
            $(this).kendoDatePicker({
                change: function (e) {

                    input.removeClass("error");

                },
                format: "MM/dd/yyyy",
                parseFormats: ["MM/dd/yyyy"]
               
            });
            $(this).keypress(function (e) {
                var key = (e.keyCode ? e.keyCode : e.which);

                if (String.fromCharCode(key).match(/[^0-9\/]/g)) return false;
                 return true;
            });

        });

    }
    
    
    
    function formattHtmlComponents(){
        $(".datetimeclass").each(function() {
            var input=$(this);
            $(this).kendoDatePicker({
                change: function (e) {

                    input.removeClass("error");

                },
                format: "MM/dd/yyyy",
                parseFormats: ["MM/dd/yyyy"]
            });
            $(this).keypress(function (e) {
                var key = (e.keyCode ? e.keyCode : e.which);

                if (String.fromCharCode(key).match(/[^0-9\/]/g)) return false;
                return true;
            });

        });

        formattHtmlSimpleComponents();

    }

    function formattHtmlSimpleComponents(){



        $(".decimal").keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);

            if (String.fromCharCode(key).match(/[^0-9-.]/g)) return false;

            var val = $(this).val();
            var regex = /^(\+|-)?(\d*\.?\d*)$/;


            if (regex.test(val + String.fromCharCode(event.charCode))) {


                return true;
            }
            return false;
        });



        $(".decimalDos").keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);

            if(key==8){ return true;}

            if (String.fromCharCode(key).match(/[^0-9-.]/g)) return false;

            var val = $(this).val();
            var regex = /^\s*-?(\d+(\.\d{0,2})?|\.\d{0,2})\s*$/;
 
            if (regex.test(val + String.fromCharCode(event.charCode))) {

                return true;
            }else{

                var el = $(this).get(0);
                var pos = 0;
                if('selectionStart' in el) {
                    pos = el.selectionStart;
                }

                if(pos <= (val + String.fromCharCode(event.charCode)).indexOf('.')){
                    return true;
                }

               // return true;
            }
            return false;
        });

 

        $(".decimalOne").keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);

            if(key==8){ return true;}

            if (String.fromCharCode(key).match(/[^0-9-.]/g)) return false;

            var val = $(this).val();
            var regex = /^\s*-?(\d+(\.\d{0,1})?|\.\d{0,1})\s*$/;

            if (regex.test(val + String.fromCharCode(event.charCode))) {

                return true;
            }else{

                var el = $(this).get(0);
                var pos = 0;
                if('selectionStart' in el) {
                    pos = el.selectionStart;
                }

                if(pos <= (val + String.fromCharCode(event.charCode)).indexOf('.')){
                    return true;
                }


            }
            return false;
        });
        
        

        $(".decimalFour").keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);

            if(key==8){ return true;}

            if (String.fromCharCode(key).match(/[^0-9-.]/g)) return false;

            var val = $(this).val();
            var regex = /^\s*-?(\d+(\.\d{0,4})?|\.\d{0,4})\s*$/;

            //console.log(val);
            //console.log(val + String.fromCharCode(event.charCode));


            if (regex.test(val + String.fromCharCode(event.charCode))) {

                return true;
            }else{

                var el = $(this).get(0);
                var pos = 0;
                if('selectionStart' in el) {
                    pos = el.selectionStart;
                }

                if(pos <= (val + String.fromCharCode(event.charCode)).indexOf('.')){
                    return true;
                }


            }
            return false;
        });
 

        $('.alphanumeric').keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (key == 46) return true;
            if (key == 32) return true;
            if (String.fromCharCode(key).match(/[^a-zA-Z0-9-]/g) && key != 8) return false;

            return true;
        });

        $('.alphanumericOnly').keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);

            if (String.fromCharCode(key).match(/[^a-zA-Z0-9-]/g)) return false;

            return true;
        });

        $('.alphanumericNormal').keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (key == 32) return true;
            if (key == 46) return true;
            if (String.fromCharCode(key).match(/[^a-zA-Z0-9-]/g) && key != 8 && key != 243 && key != 225 && key != 223 && key != 237 && key != 250 ) return false;

            return true;
        });

        $('.alpha').keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (key == 32) return true;
            if (key == 46) return false;
            if (String.fromCharCode(key).match(/[^a-zA-Z]/g) && key != 8 && key != 243 && key != 225 && key != 223 && key != 237 && key != 250 ) return false;

            return true;
        });


        $(".numericOnly").keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (String.fromCharCode(key).match(/[^0-9]/g) && key != 8) return false;
            return true;
        });

        $('.username').keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (key == 32) return false;
            if (key == 46) return true;
            if (String.fromCharCode(key).match(/[^a-zA-Z0-9-]/g) && key != 8 && key != 243 && key != 225 && key != 223 && key != 237 && key != 250 ) return false;

            return true;
        });

        $(".phone").keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (key == 32) return true;
            if (String.fromCharCode(key).match(/[^0-9()-]/g) && key != 8) return false;
            return true;
        });

        $('.alphaAutocomplete').keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (key == 32) return true;
            if (key == 46) return true;
            if (String.fromCharCode(key).match(/[^a-zA-Z,"'-]/g) && key != 8 && key != 243 && key != 225 && key != 223 && key != 237 && key != 250 ) return false;

            return true;
        });

        $(".cleanError").keypress(function (event) {
            $(this).removeClass("errorCombo")
            return true;
        });



        /*$(".datetimeclass").keypress(function (e) {
         if (String.fromCharCode(e.keyCode).match(/[^0-9\/]/g)) return false;
         return true;
         });*/
    }


    function gridReadActionAjax( url,options, jsonObject) {

        var queryString=jsonToPageableQueryParam(options,jsonObject);
        ServerHandlerModule.callServer(url+'?'+queryString,"get",{},function (result) {
                options.success(result);
              SecurityModule.securityPermission();
        },function(){
               options.error();
        }
        );
            }


    function jsonToPageableQueryParam(options, jsonObject) {
        jsonObject.skip= options.data.skip;
        jsonObject.take=options.data.take;
        jsonObject.pageSize=options.data.pageSize;
        jsonObject.page=options.data.page;

        var observable = new kendo.data.ObservableObject(jsonObject);
        var json = observable.toJSON();
        //var jsonFilter=JSON.stringify(json);
        //var queryString=jQuery.param( json );
        var queryString=jsonToQueryParam(json);

        return queryString;
    }
    function jsonToQueryParam(obj, prefix) {
        var str = [];
        for (var p in obj) {
            if (obj.hasOwnProperty(p)) {
                var k = prefix ? prefix + "[" + p + "]" : p,
                    v = obj[p];
                var jsonValue=JSON.stringify(v);
                if(jsonValue!=='null'){
                    str.push( encodeURIComponent(k) + "=" + encodeURIComponent(v));
                }

                //str.push(typeof v == "object" ? jsonToQueryParam(v, k) : encodeURIComponent(k) + "=" + encodeURIComponent(v));
            }
        }
        return str.join("&");
    }

    function autocompleteDataSource(url, callback){

        var dataSource = new kendo.data.DataSource({
            type: "application/json",
            serverFiltering: true,
            transport: {
                read: url,

                parameterMap: function (data, action) {
                    if(action === "read") {

                        var params= {
                            query: encodeURIComponent(data.filter.filters[0].value)
                        };
                        var paramValues=params;

                        if (typeof callback === "function") {
                            paramValues=_.extend(params, callback());
                        }


                        return paramValues;
                    } else {
                        return data;
                    }
                }
            }
        });

        return dataSource;
    }
    
    
    function showNotificationError(message){
        showNotification('Error',message,'error');
    }

    function showNotificationInfo(message){
        showNotification('Information',message,'info');
    }

    function showNotificationInfoRouted(message){
        showNotification('Information',message,'inforouted');
    }


    function showNotificationWarn(message){
        showNotification('Warning',message,'warning');
    }

    function showNotificationSuccess(message){
        showNotification('Success',message,'success');
    }

    function cleanNotification(){
        popupNotification.getNotifications().parent().remove();
    }
    function showNotification(title,message, type){
        //"info"
        //"success"
        //"warning"
        //"error"
        //popupNotification.hide();
        //popupNotification.getNotifications().parent().remove();
        cleanNotification();
        popupNotification.show({
            title: title,
            message: message
        }, type);
    }


    function downloadFile(url){
        if ($("#export_file").length > 0) {
            $("#export_file").remove();
        }
        if ($("#export_file").length === 0) {
            var el = document.createElement("iframe");
            document.body.appendChild(el);
            $(el).hide();
            $(el).attr("id", "export_file");
            $(el).attr("src", url);
        }
    }


    function navigateToCache(url){
        window.location.href = url;
    }

    function navigationStart(url) {

        navigationStory.push(url);

        console.log("navigationStory.push:"+url );
        //navigateTo(url);
    }

    function navigationBack() {

        var url=navigationStory.pop();

        console.log("navigationBack:"+url );

        navigateTo(url);
    }

    function navigateTo(url,json){

        var target=url;
        var queryString='';
        var additionalParams="nocache=" + (new Date()).getTime();

        if(json){
            queryString=jsonToQueryParam(json);
        }

        if(target.indexOf('?')>0){
            target=target+'&'+additionalParams;
        }else{
            target=target+'?'+additionalParams;
        }

        if(queryString!=='' ){
            target=target+'&'+queryString;
        }
        console.log(target);
        window.location.href = target;
    }

    function isBlank(str) {
        return (!str || /^\s*$/.test(str));
    }

    // New function to map url calls
     function mapUrls(parent, urls){

      $.each(urls,function(index,value){

            parent.get(value, function (context) {

                var path = context["path"];
                var targetUrl = path.substring(context["path"].lastIndexOf("/")+1);
                var partialViewUrl = targetUrl.replace(/\./g , "/");
                context.render('/' + partialViewUrl, {}, function(view) {
                    console.log(view);
                    $('#appzone').html(view);
                });

            });
            
        });
        
    }

    //  API
    return {
        ///Search
        init:init,
        jsonToQueryParam:jsonToQueryParam,
        jsonToPageableQueryParam:jsonToPageableQueryParam,
        formattHtmlComponents:formattHtmlComponents,
        formattHtmlSimpleComponents:formattHtmlSimpleComponents,
        gridReadActionAjax:gridReadActionAjax,
        autocompleteDataSource:autocompleteDataSource,
        showNotification:showNotification,
        showNotificationError:showNotificationError,
        showNotificationInfo:showNotificationInfo,
        showNotificationInfoRouted:showNotificationInfoRouted,
        showNotificationSuccess:showNotificationSuccess,
        showNotificationWarn:showNotificationWarn,
        blockProgress:blockProgress,
        unBlockProgress:unBlockProgress,
        downloadFile:downloadFile,
        navigateTo:navigateTo,
        navigateToCache:navigateToCache,
        isBlank:isBlank,
        cleanNotification:cleanNotification,
        navigationBack:navigationBack,
        navigationStart:navigationStart,
        mapUrls:mapUrls,
        formattHtmlComponentsDatetime:formattHtmlComponentsDatetime
    };
})();


var AutoCompleteModule = (function () {


    function autoCompleteConfigSetting(htmlComponent,settings,url, selectEvent,cleanEvent,jsonAdditionalFilters){
        //class is used to validate if there is a selected value
        var autocomplete_valid_value='auto-valid-value';
        var defaults ={
            filter: "contains",
            minLength: 3,
            dataSource:CommonModule.autocompleteDataSource(url,jsonAdditionalFilters),
            select: function(e) {
                var selectedItem = this.dataItem(e.item.index());

                var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                selectEvent(selectedItem, viewModel);
                htmlComponent.parent().children().addClass(autocomplete_valid_value);
            }
            ,
            change: function(e) {

                if(!htmlComponent.parent().children().hasClass(autocomplete_valid_value)){
                    var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                    this.value('');
                    cleanEvent(e,viewModel);
                    htmlComponent.parent().children().removeClass(autocomplete_valid_value);
                    htmlComponent.parent().children().removeClass("errorCombo");
                }

            },
            close: function(e){
                if(!htmlComponent.parent().children().hasClass(autocomplete_valid_value)){
                    //this.value('');
                    var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                    cleanEvent(e,viewModel);
                    htmlComponent.parent().children().removeClass("errorCombo");
                    htmlComponent.parent().children().removeClass(autocomplete_valid_value);
                }
            },filtering: function(e){
                if(!e.filter.value){
                    e.preventDefault();
                }
            }
        };

        $.extend( defaults, settings );

        htmlComponent.kendoAutoComplete(defaults);

        htmlComponent.on("input", function(e) {
            htmlComponent.parent().children().removeClass("errorCombo");

            if(htmlComponent.parent().children().hasClass(autocomplete_valid_value)){
                htmlComponent.parent().children().removeClass(autocomplete_valid_value);
                cleanEvent(e,htmlComponent.get(0).kendoBindingTarget.source);
            }

        });

    }

    function autoCompleteConfig(htmlComponent,dataTextField,url, selectEvent,cleanEvent,jsonAdditionalFilters,minLength){
        if(minLength == null){minLength=3;}
        var options={
            dataTextField: dataTextField,
            template:  '<span>#: data.'+dataTextField+' #</span>',
            minLength: minLength,
        }
        autoCompleteConfigSetting(htmlComponent,options,url,selectEvent,cleanEvent,jsonAdditionalFilters);

       /* var autocomplete_valid_value='auto-valid-value';
         if(minLength == null){minLength=3;}

        htmlComponent.kendoAutoComplete({
            dataTextField: dataTextField,
            template:  '<span>#: data.'+dataTextField+' #</span>',
            filter: "contains",
            minLength: minLength,
            dataSource:CommonModule.autocompleteDataSource(url,jsonAdditionalFilters),
            select: function(e) {
                var selectedItem = this.dataItem(e.item.index());

                var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                selectEvent(selectedItem, viewModel);
                htmlComponent.parent().children().addClass(autocomplete_valid_value);
            }
            ,
            change: function(e) {

                if(!htmlComponent.parent().children().hasClass(autocomplete_valid_value)){
                    var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                    this.value('');
                    cleanEvent(e,viewModel);
                    htmlComponent.parent().children().removeClass(autocomplete_valid_value);
                }
            },
            close: function(e){
            if(!htmlComponent.parent().children().hasClass(autocomplete_valid_value)){
                this.value('');
                    var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                    cleanEvent(e,viewModel);
                htmlComponent.parent().children().removeClass(autocomplete_valid_value);
                }
            }

        });

        htmlComponent.on("input", function(e) {
            htmlComponent.parent().children().removeClass("errorCombo");
            htmlComponent.parent().children().removeClass(autocomplete_valid_value);
            cleanEvent(e,htmlComponent.get(0).kendoBindingTarget.source);
        });*/
    }

    function autoCompleteBasicConfig(htmlComponent,dataTextField,url, selectEvent,cleanEvent,jsonAdditionalFilters,minLength){
        if(minLength == null){minLength=3;}
        var options={
            dataTextField: dataTextField,
            template:  '<span>#: data.'+dataTextField+' #</span>',
            minLength: minLength,
            valuePrimitive: true,
        }
        autoCompleteConfigSetting(htmlComponent,options,url,selectEvent,cleanEvent,jsonAdditionalFilters);

        /*if(minLength == null){minLength=3;}
        htmlComponent.kendoAutoComplete({
            dataTextField: dataTextField,
            template:  '<span>#: data.'+dataTextField+' #</span>',
            filter: "contains",
            minLength: minLength,
            dataSource:CommonModule.autocompleteDataSource(url,jsonAdditionalFilters),
            select: function(e) {
                htmlComponent.parent().children().removeClass("errorCombo");
                var selectedItem = this.dataItem(e.item.index());
                var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                selectEvent(selectedItem, viewModel);
            }
        });
        htmlComponent.on("input", function(e) {
            htmlComponent.parent().children().removeClass("errorCombo");
            cleanEvent(e,htmlComponent.get(0).kendoBindingTarget.source);
        });

        */
    }
 


    //  API
    return {
        autoCompleteConfig:autoCompleteConfig,
        autoCompleteConfigSetting:autoCompleteConfigSetting,
        autoCompleteBasicConfig:autoCompleteBasicConfig
    };
})();


var GridModule = (function () {

    function kendoFastRedrawRow(grid, row) {
        var dataItem = grid.dataItem(row);

        var rowChildren = $(row).children('td[role="gridcell"]');

        for (var i = 0; i < grid.columns.length; i++) {

            var column = grid.columns[i];
            var template = column.template;
            var cell = rowChildren.eq(i);

            if (template !== undefined) {
                var kendoTemplate = kendo.template(template);

                // Render using template
                cell.html(kendoTemplate(dataItem));
            } else {
                var fieldValue = dataItem[column.field];

                var format = column.format;
                var values = column.values;

                if (values !== undefined && values != null) {
                    // use the text value mappings (for enums)
                    for (var j = 0; j < values.length; j++) {
                        var value = values[j];
                        if (value.value == fieldValue) {
                            cell.html(value.text);
                            break;
                        }
                    }
                } else if (format !== undefined) {
                    // use the format
                    cell.html(kendo.format(format, fieldValue));
                } else {
                    // Just dump the plain old value
                    cell.html(fieldValue);
                }
            }
        }
    }

    function dataTableDsConfig(htmlComponent, modelFields,url,paramValues, serverPaging, htmlLabelResultCount){

        var   serverPagingProp=true;

        if(serverPaging!=null){
            serverPagingProp=serverPaging;
        }

        var dataSourceGrid = new kendo.data.DataSource({
            type: "application/json",
            serverSorting:true,
            serverPaging:serverPagingProp,
            serverFiltering:true,
            schema: {
                total: "total",
                data:"resultList",
                model: {
                    fields: modelFields
                }
            },
            transport: {
                read: function (options) {
                	CommonModule.gridReadActionAjax(url,options,paramValues );            
                }
            },
            change: function(e) {
                if (htmlLabelResultCount != null){
                	htmlLabelResultCount.text(this.total());                	
                }
            },
            pageSize: 10
        });


        var grid = htmlComponent.data("kendoGrid");

        grid.setDataSource(dataSourceGrid);
    }



    //  API
    return {
        dataTableDsConfig:dataTableDsConfig,
        kendoFastRedrawRow:kendoFastRedrawRow
    };
})();


var WindowModule = (function () {

    function showDialog(htmlComponentId, url,settings){

       var defaults = {
           actions: ["Close"],
           draggable: true,
           height: "350px",
           modal: true,
           contentUrl: url,
           deactivate: function() {
               this.destroy();
           },
           // resizable: false,
           title: "Messages",
           width: "800px",
           visible: false


       };

        $.extend( defaults, settings );

        var logMessageWnd = $('<div id="'+htmlComponentId+'" style="visibility: hidden">').kendoWindow(defaults);

        logMessageWnd.data("kendoWindow").center().open().refresh(url);

    }

    function showDialog2(htmlComponentId, url){

        //var direccion = "/lane/log?rfqId=" + rfqId;

        var logMessageWnd = $('<div id="'+htmlComponentId+'" style="visibility: hidden">').kendoWindow({
            actions: ["Close"],
            draggable: true,
            height: "480px",
            modal: true,
            contentUrl: url,
            deactivate: function() {
                this.destroy();
            },
            // resizable: false,
            title: "Messages",
            width: "800px",
            visible: false

        });

        logMessageWnd.data("kendoWindow").center().open().refresh(url);

    }


    //  API
    return {
        showDialog:showDialog,
        showDialog2:showDialog2
    };
})();


var ServerHandlerModule = (function () {

    function callServer( url,type,data, successCallBack,errorCallBack){
        CommonModule.blockProgress();
        var response=false;
        $.ajax({
            url: url,
            type: type,
            data: data,
            dataType:'json',
            contentType: "application/json",
            success: function (result) {
                CommonModule.unBlockProgress();
                var errorFlag=false;
                var error=JSON.stringify(result);
                console.log("ServerHandlerModule :: callServer : success"+ JSON.stringify(error));

                if (error.indexOf("logref") > 0) {
                    var errorList = $.parseJSON(error);

                    if(errorList.length>0){
                        CommonModule.showNotificationError(errorList[0].message);
                        errorFlag=true;
                    }


                }

                if (typeof errorCallBack === "function") {
                    if(errorFlag){
                        errorCallBack();
                    }
                }

                if(!errorFlag){
                    if (typeof successCallBack === "function") {
                        response= successCallBack(result);
                    }
                }

            }
            ,
            error: function (xhr, status, error) {
                CommonModule.unBlockProgress();
                console.log("ServerHandlerModule :: callServer : error"+ JSON.stringify(xhr));
                CommonModule.showNotificationError(error);

            }
        });

        return response;

    }



    //  API
    return {
        callServer:callServer,
    };
})();

var DropdownBox = (function () {

    function getEnumList(enumName){

        return arrayEnums[enumName];
    }

    function getItemEnumList(enumName,valueItem){

       for( i=0;i<enumName.length;i++ ) {
            if (enumName[i]["code"] == valueItem) {
                return enumName[i];
            }

        }
       return null;
    }
    //  API
    return {
        getEnumList:getEnumList,
        getItemEnumList:getItemEnumList
    };
})();

var ValidationModule = (function () {

    function isAlphanumeric(event) {
        var key = (document.all) ? event.keyCode : event.which;

        if ((key >= 48 && key <= 57) || (key >= 65 && key <= 90) || (key >= 97 && key <= 122) || key == 209 || key == 241 || key == 0 || key == 8) {
            return true;
        }
        return false;
    }

    function textObjectUpperCase(textObject) {
        textObject.value = textObject.value.toUpperCase() ;
    }

    function isNumber(event) {
        var key = (document.all) ? event.keyCode : event.which;

        if ((key >= 48 && key <= 57) || key == 0 || key == 8) {
            return true;
        }
        return false;
    }


    function validateMaxLength(object, maxLengthValue){
        if(object != null && maxLengthValue != null
            && object.value.length > maxLengthValue){
            object.value = object.value.substr(0, maxLengthValue);
        }
        return;
    }
    //  API
    return {
        isNumber:isNumber,
        isAlphanumeric:isAlphanumeric,
        textObjectUpperCase:textObjectUpperCase,
        validateMaxLength:validateMaxLength
    };

})();
var DialogModule = (function () {

    function showConfirmationDialog(message, yesCallBack,settings){
        var defaults={
            title:'Confirmation',
            visible: false, //the window will not appear before its .open method is called
            width: "400px",
            height: "200px",
        };

        $.extend( defaults, settings );

        var windowConfirmation = $("#confirmationWnd").kendoWindow(
            defaults

        ).data("kendoWindow");


        var windowTemplate = kendo.template($("#confirmationWndTemplate").html());

        windowConfirmation.content(windowTemplate({message:message})); //send the row data object to the template and render it
        // window.open().center();
        windowConfirmation.center().open()


        $("#yesButton").click(function(){
            yesCallBack();
            windowConfirmation.close();
        })
        $("#noButton").click(function(){
            //noCallBack();
            windowConfirmation.close();
        })


    }
   

    //  API
    return {
        showConfirmationDialog:showConfirmationDialog,
       
    };
})();


var SecurityModule = (function () {

    function securityPermission(readonly ){
        var localReadOnly=false;

        if (readonly !== undefined){
            localReadOnly=readonly;
        }else if (readOnlyModule !== undefined) {
            localReadOnly=readOnlyModule;
        }




        console.log("securityPermission :: readonly?"+ localReadOnly);

        //securityPermissions esta variable se carga cuando se pinta el menu
        $(":file").each(function () {
            //validateResourcePermission(this.id);
            console.log("file:"+ this.id);

            if (localReadOnly) {
                $(this).css('display', "none");
            } else {
                $(this).css('display', 'block');
            }
        });

        $(".k-grid-delete").each(function () {
            permissionButton(localReadOnly, this);
        });

        $(".k-grid-edit").each(function () {
            //permissionButton(localReadOnly, this);
        });

        $(".k-grid-update").each(function () {
            permissionButton(localReadOnly, this);
        });



        $(".k-grid-add").each(function () {
            permissionButton(localReadOnly, this);
        });

        $(".action-updateable").each(function () {
            permissionButton(localReadOnly, this);

        });

        //
        // $(".action-updatable").each(function () {
        //     console.log("button:"+ this.id);
        //     if (readonly) {
        //         //$(this).css('display', "none");
        //         $(this).prop( "disabled", true );
        //     } else {
        //         $(this).prop( "disabled", false );
        //         //$(this).css('display', 'block');
        //     }
        // });

        $(".k-dropdown .input-updateable").each(function () {

            console.log("input-dropdown:"+ this.id);
            if (localReadOnly) {

                //$(this).css('display', "none");
                // $(this).prop('disabled', true);
                // $(this).attr("onclick", "");
                // $(this).attr("onclick", "");
                // $(this).unbind("click");
                // $(this).removeAttr("onclick");
                var dropdownlist = $("#"+this.id).data("kendoDropDownList")

                if(dropdownlist){
                    dropdownlist.readonly();
                }

            } else {

                //$(this).css('display', 'block');
                //$(this).prop('disabled', false);
            }
        });


        //k-datepicker
        $(".datetimeclass .input-updateable").each(function () {

            console.log("kendoDatePicker:"+ this.id);
            if (localReadOnly) {
                var datepicker = $(this).data("kendoDatePicker");
                if(datepicker){
                    datepicker.readonly();
                }
            }
        });



        //k-autocomplete

        $(".input-updateable").each(function () {

            if (localReadOnly) {
                $(this).prop('readonly', true).prop('disabled', true);
            }

            /*
            else {
                $(this).prop('readonly', false).prop('disabled', false);
            }
            */
        });


    }

    function permissionButton(readOnly, htmlElement){
        console.log("button:"+ this.id);
        if (readOnly) {
            $(htmlElement).css('display', "none");
            $(htmlElement).prop( "disabled", true );
            $(htmlElement).attr('disabled', true);
            $(htmlElement).unbind("click");
            $(htmlElement).attr("href", "#");
            $(htmlElement).attr("onclick", "");
            $(htmlElement).removeAttr("onclick");
            $(htmlElement).click(function(e){
                e.stopPropagation();
                e.preventDefault();
                e.stopImmediatePropagation();
                return false;
            });

        } else {
           // $(htmlElement).css('display', 'block');
            $(htmlElement).prop( "disabled", false );
            $(htmlElement).attr('disabled', false);
        }
    }



    //  API
    return {
        securityPermission:securityPermission,
    };
})();


var ConfirmModule = (function () {

    function showConfirmationDialog(question, yesCallBack,noCallBack){
        $('#MyConfirmModal').find('.modal-question').text(question);
        $('#MyConfirmModal').modal('show');

        $('#confirm-yes').one("click", function() {
             yesCallBack();
            $('#MyConfirmModal').modal('hide');
        });
        $('#confirm-no').click(function () {
            $('#MyConfirmModal').modal('hide');
        });

        $('#confirm-no').one("click", function() {

            $( "#confirm-yes" ).unbind();
            $('#MyConfirmModal').modal('hide');

            if(typeof(noCallBack) == "function")
                noCallBack();
        });

        $('#confirm-close').one("click", function() {
            $( "#confirm-yes" ).unbind();
            $( "#confirm-no" ).unbind();
            $('#MyConfirmModal').modal('hide');
         });


    }

    //  API
    return {
        showConfirmationDialog:showConfirmationDialog,

    };

})();
