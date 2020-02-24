

var caseData;

function showCaseDetails(id) {
    debugger;
}


function buildCasesTable() {
    var data = caseData || null;
    var html = '';
    if (!data || !data.length) $('#CaseTable').text("Fehler");
    data.forEach(function(row) {
        html += "<tr id='" + row.caseNumber + "'>";
        html += "   <td>" + row.caseNumber + "</td>";
        html += "   <td>" + row.clinicId + "</td>";
        html += "   <td class='service-count'>#" + Object.keys(row.caseServices).length + " Services</td>";
        html += "</tr>";
        html += "<tr id='details_" + row.caseNumber + "' class='hidden'></tr>";
    })

    $('#CaseTable tbody').html(html);
    $('.service-count').on("click touchend", function() {
        var caseId = $(this).parent().attr('id');
        showCaseDetails(caseId);
    })
}

$(function () {

    $.ajax({
        method: "GET",
        url: "http://192.168.31.2:8585/webapp/resources/casepool/cases",
    }).done(function (data) {
        caseData = data;
        buildCasesTable();
    })
    .fail(function (err) {
        alert("error");
    })

});