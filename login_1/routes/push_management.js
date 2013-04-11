exports.push_page= function (req, res) {
    console.log("push page");
    res.render('push_page', {
        title: 'push page'
    });
}