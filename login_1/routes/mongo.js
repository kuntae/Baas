exports.mongoget = function (req, res) {
    console.log("get mongo");

    mongoose.connect('mongodb://localhost/mydb');



}