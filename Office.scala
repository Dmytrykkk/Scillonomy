import Main.platform

class Office(private var _officeListStudents: List[Student] = List(),
             private var _officeListCoaches: List[Coach] = List()){
  this.addToPlatdorm(this)

  private var _courseList: List[Course] = Nil

  def officeListStudents: List[Student] = _officeListStudents;
  def officeListCoaches: List[Coach] = _officeListCoaches;
  def courseList: List[Course] = _courseList

  def addStudent(newStudent: Student): Unit = {
    _officeListStudents = _officeListStudents :+ newStudent

  }

  def removeStudent(student: Student): Unit = {
    if (_officeListStudents.contains(student)) {
      _officeListStudents = _officeListStudents.filter(_ != student)
    } else {
      println("Student not found")
    }
  }

  def addCoach(newCoach: Coach): Unit = {
    _officeListCoaches = _officeListCoaches :+ newCoach
    val course = new Course {
      override def courseID: Int = newCoach.courseID

      override def coursePrice: Int = newCoach.coursePrice

      override def courseDuration: Int = newCoach.courseDuration
    }
    _courseList = course :: _courseList
    platform.addCourse(course)
  }

  def removeCoach(coach: Coach): Unit = {
    if (_officeListCoaches.contains(coach)) {
      _officeListCoaches = _officeListCoaches.filter(_ != coach)
    } else {
      println("Coach not found")
    }
  }

  private def addToPlatdorm(office: Office): Unit = {
    platform.addOffice(office)
  }
}
