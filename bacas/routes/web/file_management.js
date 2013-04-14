exports.file_page= function (req, res) {
    console.log("file page");
    res.render('file_page', {
        title: 'file page'
    });
}