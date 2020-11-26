$(document).ready(function() {

    let tdTableObj;             //Corresponds to the data in the ThingDefStatsTable
    let thTableObj;             //Corresponds to the data in the ThingStatsTable
    let tableList = new Object;
    let original;

    var attrTable = $('#powerdefTable').DataTable( {
        data: tablePowerdefData,
        "columns": [
            { "data": "id" },
            { "data": "name", "width": "30%" },
            { "data": "description"},
        ]
    } );

    var attrTable = $('#attributeTable').DataTable( {
        data: tableAttributeData,
        "columns": [
            { "data": "id" },
            { "data": "name" },
            { "data": "hasvalue"},
            { "data": "attributeByIdAttribute"},
        ]
    } );

    var thingDefTable = $('#thingDefTable').DataTable( {
        data: tableThingDefData,
        "columns": [
            { "data": "id" },
            { "data": "name", "width": "30%" },
            { "data": "description"}
        ]
    } );

    var thingTable = $('#thingTable').DataTable( {
        data: tableThingData,
        "columns": [
            { "data": "id" },
            { "data": "name" },
            { "data": "count"},
            {"data": "thingdefByIdThingdef"},
            {"data": "description"}
        ]
    } );
    //By using a JSON formatted object, instead of an array, we can selectively choose which key/value from the json object
    //we will display when initializing the datatable. Table will behave the same even when passed data as objects vars
    var tdStatDataInit = [{"defname":"default","attrname":"default","stat":0}];

    const createdCell = function(cell) {
        cell.setAttribute('contenteditable', true)
        cell.setAttribute('spellcheck', false)
        cell.addEventListener('focus', function(e) {
            original = e.target.textContent
        })
        //Blur is similar to lost focus event but it doesn't bubble up (parent element eventListeners not triggered)
        //The challenge was understanding that changing the table values does not change the underlying cached data
        //object.  To do that, as below, we need to explicitly call the data() API for that cell and assign
        //The cell value, referenced by e.target.textContent
        cell.addEventListener('blur', function(e) {
            if (original !== e.target.textContent) {
                let tblName= e.target.closest("table").getAttribute("id");
                //const row = tableList[tblName].row(e.target.parentElement);
                tableList[tblName].cell(this).data(e.target.textContent).draw();
                console.log(tableList[tblName].rows().data());
            }
        })

    }

    tableList.tdStatTable = $('#tdStatTable').DataTable( {
        data: tdStatDataInit,
        columnDefs: [{
            targets: '_all',
            createdCell: createdCell
        }],
        "columns": [
            { "data": "attrname"},
            { "data": "stat"},
        ],
        "bSort" : false,
        "aaSorting" : [[]],
        "order": []         //This is required to prevent DataTable from auto ordering the data, which we don't want
    } );

    tableList.thStatTable = $('#thStatTable').DataTable( {
        "columns": [
            { "data": "attrname"},
            { "data": "stat"},
        ],
        //"keys": true,
        columnDefs: [{
            targets: '_all',
            createdCell: createdCell
        }],
        "bSort" : false,
        "aaSorting" : [[]],
        "order": []         //This is required to prevent DataTable from auto ordering the data, which we don't want
    } );

    $('#thingDefTable tbody').on( 'click', 'tr', function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            thingDefTable.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
            $("#txtTDefName").val(thingDefTable.row(this).data().name);
            $("#txtTDefDescription").val(thingDefTable.row(this).data().description);
        }
    } );

    $( "#btnShowThingStats" ).click(function() {
        let data = tableList["thStatTable"].rows().data();
        alert(JSON.stringify(data));
    });

    $( "#btnNewThingDef" ).click(function() {
        alert("newThingDef Button Clicked");
        let newThingDef = new Object();
        let newTDObj = new Object();
        newThingDef.name = $("#txtTDefName").val();
        newThingDef.description = $("#txtTDefDescription").val();
        newTDObj["thingdef"] = newThingDef;
        $.ajax({
            url: '/rest-api/addThingDef',
            method: 'POST',
            contentType : 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(newTDObj),
            success: function(response) {
                thingDefTable.row.add(response).draw();
                alert("Successful Post to Spring Controller with ThingDef data " + response);
            }
        })
    });
    $( "#btnCloneStats" ).click(function() {
        let cloneRequest = new Object();
        cloneRequest["tdID"] = thingDefTable.row('.selected').data().id;
        cloneRequest["tdStatsList"] = tdTableObj;
        $.ajax({
            url: '/rest-api/cloneThingDef',
            method: 'POST',
            contentType : 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(cloneRequest),
            success: function(response) {
                switch(response) {
                    case 1:
                        alert("Thingdef object cloned");
                        break;
                    case 0:
                        alert("Stats already exists, please use update method");
                        break;
                }
            }
        })
    });

    $( "#btnUpdateStats" ).click(function() {
        alert("The Update ThingDef Button Clicked");
        let updateRequest = new Object();
        updateRequest["tdID"] = thingDefTable.row('.selected').data().id;
        updateRequest["tdStatsList"] = tdTableObj;
        updateRequest["name"] = $("#txtTDefName").val();
        updateRequest["description"] = $("#txtTDefDescription").val();
        $.ajax({
            url: '/rest-api/updateThingDef',
            method: 'POST',
            contentType : 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(updateRequest),
            success: function(response) {
                switch(response) {
                    case 0:
                        alert("No stats updated");
                        break;
                    default:
                        alert(response + " Thingdefstats records updated");
                        break;
                }
            }
        })
    });

    $( "#btnLoadThingDefStats" ).click(function() {
        let id = thingDefTable.row('.selected').data().id;
        let request = new Object();
        request["tdID"] = id;

        $.ajax({
            url: '/rest-api/getThingDefStats',
            method: 'POST',
            contentType : 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(request),
            success: function(response) {
                //There are multiple ways to 'copy' an object, the parsing of the same object stringified was
                //given as a 'deep' copy method.  We don't want to pass references, as the data will be editable
                //in two different tables
                tdTableObj = JSON.parse(JSON.stringify(response));
                thTableObj = JSON.parse(JSON.stringify(response));
                traverse(tdTableObj,process);
                tableList["tdStatTable"].clear().rows.add(tdTableObj).draw();
                tableList["thStatTable"].clear().rows.add(thTableObj).draw();
            }
        })
    });

    //*** AttributeTable Functions
    $('#attributeTable tbody').on( 'click', 'tr', function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            attrTable.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    } );

    $('#attributeTable tbody').on( 'click', 'tr', function () {
        //var numId = attrTable.rows('.selected').data();
        var selectedID = attrTable.row(this).data().id;
        console.log( attrTable.row(this).data().id );
        $("#txtParentid").val(selectedID);
    } );


    $( "#btnNewAttribute" ).click(function() {

        let formData = new Object();
        let attrObj = new Object();
        let parentId = null;
        if (!$("#txtParentid").val()) {
            parentId = $("#txtParentid").val();
        }
        attrObj.name = $("#txtName").val();
        attrObj.hasvalue = $("#txtHasvalue").val();

        formData["attribute"] = attrObj;
        formData["parentId"] = parentId;

        //alert( "Sent" + JSON.stringify(formData) );
        $.ajax({
            url: '/rest-api/addAttribute',
            method: 'POST',
            contentType : 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(formData),
            success: function(response) {
                attrTable.row.add(response).draw();
                alert("Successful Post to Spring Controller with data " + response);
            }
        })
    });

    //*****Sample code to traverse json objects.  We may need this to build certain tables from the objects
    //passed by the Spring Controller
    //The 'process' function called with every property and its value
    function process(key,value) {
        console.log(key + " : "+value);
    }
    //Traverse will take an object o and function func to process the json
    //That func needs the key/value pair
    function traverse(o,func) {
        for (var i in o) {
            func.apply(this,[i,o[i]]);
            if (o[i] !== null && typeof(o[i])=="object") {
                //going one step down in the object tree!!
                traverse(o[i],func);
            }
        }
    }
    //When we want to traverse the object, we can call the function as follows...
    //traverse(o,process);
} );