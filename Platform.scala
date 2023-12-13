import Main.platform
import scala.util.Random


class Platform(private var _generalTokens: Double,
               private var _tokenPrice: Double){
  private var _officeList: List[Office] = Nil
  private var _courseList: List[Course] = Nil
  private var _fiatToken: Double = _generalTokens

  def fiatToken: Double = _fiatToken
  def officeList: List[Office] = _officeList
  def generalTokens: Double = _generalTokens
  def tokenPrice: Double = _tokenPrice
  def courseList: List[Course] = _courseList


  def addCourse(course: Course): Unit = {
    _courseList = course :: _courseList
  }

  def addOffice(office: Office): Unit = {
    _officeList = _officeList :+ office
  }

  def removeOffice(office: Office): Unit = {
    if (_officeList.contains(office)) {
      _officeList = _officeList.filter(_ != office)
    } else{
        println("Office not found")
    }
  }

  def tokenBuy(amount: Double): Unit = {
      _fiatToken -= amount
  }

  def tokenSell(amount: Double): Unit= {
    _fiatToken += amount
  }

  def getRandomOffice: Option[Office] = {
    if (_officeList.nonEmpty) {
      val randomIndex = Random.nextInt(_officeList.length)
      Some(_officeList(randomIndex))
    } else {
      None
    }
  }

  def getOfficeByCoach(coach: Coach): Option[Office] = {
    _officeList.find(_.officeListCoaches.contains(coach))
  }

  def getCourseByID(courseID: Int): Option[Course] = {
    _courseList.find(_.courseID == courseID)
  }

  def newStudent(student: Student): Unit = {
    platform.getRandomOffice match {
      case Some(office) =>
        office.addStudent(student)
      case None =>
        println("No offices available")
    }
  }

  def newCoach(coach: Coach): Unit = {
    platform.getRandomOffice match {
      case Some(office) =>
        office.addCoach(coach)
      case None =>
        println("No offices available")
    }
  }

  def calculateTokenPrice(): Unit = {
    _tokenPrice = fiatToken / _generalTokens
  }
}
