exports.datatree_page= function (req, res) {
    console.log("data tree");
    res.render('datatree_page', {
        title: 'datatree page'
    });
}