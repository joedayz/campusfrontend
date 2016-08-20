
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
                if (String.fromCharCode(e.keyCode).match(/[^0-9\/]/g)) return false;
                return true;
            });

        });


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

            if (String.fromCharCode(key).match(/[^0-9-.]/g)) return false;

            var val = $(this).val();
            var regex = /^\s*-?(\d+(\.\d{0,2})?|\.\d{0,2})\s*$/;

            console.log(val);
            console.log(val + String.fromCharCode(event.charCode));


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
            if (key == 32) return true;
            if (String.fromCharCode(key).match(/[^a-zA-Z0-9-]/g) && key != 8) return false;

            return true;
        });

        $('.alphanumericNormal').keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (key == 32) return true;
            if (String.fromCharCode(key).match(/[^a-zA-Z0-9-]/g) && key != 8 && key != 243 && key != 225 && key != 223 && key != 237 && key != 250 ) return false;

            return true;
        });

        $(".numericOnly").keypress(function (event) {
            var key = (event.keyCode ? event.keyCode : event.which);
            if (String.fromCharCode(key).match(/[^0-9]/g) && key != 8) return false;
            return true;
        });


        /*$(".datetimeclass").keypress(function (e) {
         if (String.fromCharCode(e.keyCode).match(/[^0-9\/]/g)) return false;
         return true;
         });*/
    }


    function gridReadActionAjax( url,options, jsonObject) {
        // jsonObject.skip= options.data.skip;
        // jsonObject.take=options.data.take;
        // jsonObject.pageSize=options.data.pageSize;
        // jsonObject.page=options.data.page;
        //
        // var observable = new kendo.data.ObservableObject(jsonObject);
        // var json = observable.toJSON();

        var queryString=jsonToPageableQueryParam(options,jsonObject);

        CommonModule.blockProgress();

        $.ajax({

            url: url+'?'+queryString,
            type: "get",

            contentType: "application/json",
            success: function (result) {
                CommonModule.unBlockProgress();
                options.success(result);
            },
            error: function (xhr, status, error) {
                CommonModule.unBlockProgress();
                showNotificationError(error)
                //$.growl.error({ message: JSON.stringify(xhr) });
                //alert(JSON.stringify(xhr));
            }
        });

    }
    /*
     function showMessage(msge, msgeType) {
     var dialogType = msgeType == "WARNING" ? BootstrapDialog.TYPE_WARNING : msgeType == "ERROR" ? BootstrapDialog.TYPE_DANGER : BootstrapDialog.TYPE_SUCCESS;
     var msgeTitle = msgeType == "SUCCESS" ? "Mensaje" : msgeType == "WARNING" ? "Alerta" : "Error";
     BootstrapDialog.show({
     type: dialogType,
     title: msgeTitle,
     message: msge
     });
     }*/

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
                            query: data.filter.filters[0].value
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



        // var rows = $("tbody:first > tr:gt(0)", idgrid);
        //
        // if (rows.length == 0) {
        //     return;
        // }
        // var nombre = jQuery(idgrid).attr("id");

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

        window.location.href = target;
    }

    function isBlank(str) {
        return (!str || /^\s*$/.test(str));
    }

    //  API
    return {
        ///Search
        init:init,
        jsonToQueryParam:jsonToQueryParam,
        jsonToPageableQueryParam:jsonToPageableQueryParam,
        formattHtmlComponents:formattHtmlComponents,
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
    };
})();


var AutoCompleteModule = (function () {

    function autoCompleteConfig(htmlComponent,dataTextField,url, selectEvent,cleanEvent,jsonAdditionalFilters){

        htmlComponent.kendoAutoComplete({
            dataTextField: dataTextField,
            template:  '<span>#: data.'+dataTextField+' #</span>',
            filter: "contains",
            minLength: 3,
            dataSource:CommonModule.autocompleteDataSource(url,jsonAdditionalFilters),
            select: function(e) {
                var selectedItem = this.dataItem(e.item.index());

                var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                selectEvent(selectedItem, viewModel);
            },
            change: function(e) {
                var value = this.value();
                if(value==null || value===''){
                    var viewModel=htmlComponent.get(0).kendoBindingTarget.source;
                    cleanEvent(e,viewModel);
                }
                console.log("change event handler" );
            }

        });

    }



    //  API
    return {
        autoCompleteConfig:autoCompleteConfig,
    };
})();


var GridModule = (function () {

    function dataTableDsConfig(htmlComponent, modelFields,url,paramValues){

        var dataSourceGrid = new kendo.data.DataSource({
            type: "application/json",
            serverSorting:true,
            serverPaging:true,
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
            pageSize: 10
        });


        var grid = htmlComponent.data("kendoGrid");

        grid.setDataSource(dataSourceGrid);
    }



    //  API
    return {
        dataTableDsConfig:dataTableDsConfig,
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

    function callServer( url,type,data, successCallBack){
        CommonModule.blockProgress();
        $.ajax({
            url: url,
            type: type,
            data: data,
            contentType: "application/json",
            success: function (result) {
                CommonModule.unBlockProgress();
                var errorFlag=false;
                var error=JSON.stringify(result);
                console.log("ServerHandlerModule :: callServer : success"+ JSON.stringify(error));

                if (error.indexOf("logref") > 0) {
                    //var errorJson=$.parseJSON(result);
                    //var message=result.success.pop();
                    var errorList = $.parseJSON(error);

                    if(errorList.length>0){
                        CommonModule.showNotificationError(errorList[0].message);
                        errorFlag=true;
                    }


                }

                if(!errorFlag){
                    successCallBack(result);
                }

            }
            ,
            error: function (xhr, status, error) {
                CommonModule.unBlockProgress();
                console.log("ServerHandlerModule :: callServer : error"+ JSON.stringify(xhr));
                CommonModule.showNotificationError(error);

            }
        });

    }



    //  API
    return {
        callServer:callServer,
    };
})();