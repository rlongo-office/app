$(document).ready(function() {
    var attrTable = $('#attributeTable').DataTable( {
        data: tableData,
        "columns": [
            { "data": "id" },
            { "data": "name" },
            { "data": "hasvalue"},
            { "data": "attributeByIdAttribute"},
        ]
    } );

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
} );