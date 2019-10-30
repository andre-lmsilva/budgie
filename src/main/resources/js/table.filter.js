numeral.register('locale', 'ie', {
    delimiters: {
        thousands: '.',
        decimal: ','
    },
    abbreviations: {
        thousand: 'k',
        million: 'm',
        billion: 'b',
        trillion: 't'
    },
    ordinal: function (number) {
        if (number == 1) {
            return 'st';
        } else if (number == 2) {
            return 'nd';
        } else if (number == 3) {
            return 'rd';
        } else {
            return 'th';
        }
    },
    currency: {
        symbol: '€'
    }
});
numeral.locale('ie')

/*
 * Highlights the received text inside the node, wrapping the text with <em>...</em>.
 */
function highlightText(text, node) {
    let highlightedHtml = node.html().replace(text, '<em>'+text+'</em>');
    node.html(highlightedHtml);
}

/*
 * Removes the highlight around the text.
 */
function removeHighlightText(node) {
    let notHighlightedHtml = node.html().replace('<em>','').replace('</em>','');
    node.html(notHighlightedHtml);
 }

/*
 * Filters the table to only show rows that contains text that matches with the received text.
 */
function filterTable() {
    let text = $('.filter-value').val();

    if (text.length == 0) {
        resetFilteredTable();
        return;
    }

    for (let rowIndex = 1; rowIndex < $('.filterable > tbody > tr').length; rowIndex++) {
        let row = $($('.filterable > tbody > tr')[rowIndex]);
        if (row.text().includes(text)) {
            row.removeClass('d-none');
            highlightText(text, row);
        } else {
            removeHighlightText(row);
            row.addClass('d-none');
        }
    }
    sumFilteredValues();
}

/*
 * Resets a filtered table, displaying all the rows again.
 */
function resetFilteredTable() {
    for (let rowIndex = 0; rowIndex < $('.filterable > tbody > tr').length; rowIndex++) {
        let row = $($('.filterable > tbody > tr')[rowIndex]);
        row.removeClass('d-none');
        removeHighlightText(row);
    }
    $('.filter-value').val('');
    sumFilteredValues();
}

/*
 * Calculates and show the
 */
function sumFilteredValues() {
    let valueFields = $('.filterable > tbody > tr > td > .record-value');
    let balance = 0.00;
    for(let fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
        let field = $(valueFields[fieldIndex]);
        if (!field.closest('tr').hasClass('d-none')) {
            balance += parseFloat(field.val());
        }
    }

    if ($('#lblBalance').length > 0) {
        $('#lblBalance').text(
            '€ '+numeral(balance).format('0,0.00')
        );

        $('#lblBalance').removeClass('text-danger');
        $('#lblBalance').removeClass('text-info');

        let style = 'text-danger';
        if (balance >= 0) style = 'text-info';

        $('#lblBalance').addClass(style);
    }
}

