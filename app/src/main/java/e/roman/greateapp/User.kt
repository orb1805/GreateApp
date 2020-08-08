package e.roman.greateapp

class User {
    private var name : String
    private var is_18 : Boolean
    private var university : String
    constructor(name : String, is_18 : Boolean, university : String){
        this.name = name
        this.is_18 = is_18
        this.university = university
    }
}