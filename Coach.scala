import Main.platform

class Coach(_name: String, _age: Int, _address: Address, private var _coursePrice: Int, private var _courseID: Int,
            private var _courseDuration : Int)
  extends Human(_name, _age, _address) with Course {
  private var _salary: Double = 0
  private var _tokens: Double = 0
  private var _trainingPeriod: Int = 0
  private var _listStudents: List[Student] = Nil

  def tokens: Double = _tokens
  def listStudents: List[Student] = _listStudents
  def coursePrice: Int = _coursePrice
  def salary: Double = _salary
  def trainingPeriod: Int = _trainingPeriod
  def courseID: Int = _courseID
  def courseDuration: Int = _courseDuration

  this.updateOther()

  def addTrainingPeriod(): Unit = {
    _trainingPeriod += 1
  }

  private def calculateSalary(): Unit = {
    _salary = _coursePrice * _listStudents.size
  }

  def addStudent(student: Student): Unit = {
    _listStudents = _listStudents :+ student

    platform.getOfficeByCoach(this) match {
      case Some(office) =>
        office.addStudent(student)
      case None =>
        println("Office not found for the coach")
    }
  }

    def removeStudent(student: Student): Unit = {
      _listStudents = _listStudents.filter(_ != student)
    }

    def sellTokens(amount: Double): Unit = {
      if (amount > _tokens) {
        println("\nNot enough tokens")
      }
      else {
        _tokens -= amount
        platform.tokenSell(amount)
      }
    }

    def buyTokens(amount: Double): Unit = {
      _tokens += amount
      platform.tokenBuy(amount)
    }

    def payment(): Unit = {
      _tokens += _salary
    }

    private def updateOther(): Unit = {
      platform.getRandomOffice match {
        case Some(office) =>
          office.addCoach(this)
        case None =>
          println("No offices available")
      }
    }
  }
