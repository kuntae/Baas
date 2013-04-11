exports.developer_page= function (req, res) {
    console.log("developer page");
    res.render('developer_page', {
        title: 'developer_page'
    });
}