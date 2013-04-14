exports.location_page= function (req, res) {
    console.log("location page");
    res.render('location_page', {
        title: 'location page'
    });
}