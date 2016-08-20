var TemasCursoModule = (function () {
	
	
	function initTemasCurso(keyCurso) {

        configHtmlComponents(keyCurso);
        
    }
    

    function configHtmlComponents(keyCurso) {
    	

        $("#temasCursoTable").kendoGrid({
            dataSource: new kendo.data.DataSource({
                type: "application/json",
                transport: {
                    read: "/cursoApi/temasDisponibles?keyCurso=" + keyCurso
                },                
                schema: {
                    model: {
                        fields: {
                            temasCursoId: {type: "number"},
                            titulo: {type: "string"},
                            descripcion: {type: "string"},
                            url: {type: "string"},
                            temaOrden: {type: "number"},
                            status: {type: "string"},
                            cursoId: {type: "number"}
                        }
                    }
                },
                pageSize: 20,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true
            }),
            height: 550,
            filterable: true,
            sortable: true,
            pageable: true,
            columns: [{
                    field:"temaOrden",
                    title: "Tema #",
                    filterable: false,
                    width: 100
                },
                {
                    field: "titulo",
                    title: "Titulo",
                    filterable: false
                }, {
                    field: "descripcion",
                    title: "Descripcion",
                    filterable: false
                }, {
                    field: "url",
                    title: "URL",
                    template : '<a class="k-button k-button-icontext btn-primary"  href="javascript:void(0)" onclick="window.open(\'#=url#\');" >   <span> <span class="fa fa-cogs iconColor"></span>   Material y Video</span></a>',
                    filterable: false,
                    width: 200
                }
            ]
        });
        var grid = $("#temasCursoTable").data("kendoGrid");

        grid.dataSource.read();


        console.log("event :: click : Loading grid temasCurso");


    }



    //  API
    return {
        ///Search
    	initTemasCurso:initTemasCurso,
    };
    
    
    
})();