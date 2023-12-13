class Human(private var _name: String, private var _age: Int, private var _address: Address) {
  def name: String = _name
  def age: Int = _age
  def address: Address = _address

  def name_=(newName: String): Unit = _name = newName
  def age_=(newAge: Int): Unit = _age = newAge
}
