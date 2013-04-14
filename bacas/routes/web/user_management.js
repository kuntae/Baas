exports.user_page= function (req, res) {
    console.log("user page");
    res.render('user_page', {
        title: 'user page'
    });
}