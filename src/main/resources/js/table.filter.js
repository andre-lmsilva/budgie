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
}

