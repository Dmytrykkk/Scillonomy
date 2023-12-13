class Address(private var _country: String, private var _city: String) {
  def country: String = _country
  def city: String = _city

  def country_=(newCountry: String): Unit = _country = newCountry
  def city_=(newCity: String): Unit = _city = newCity

  override def toString: String = s"${_country}, ${_city}"
}
