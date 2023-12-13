import Main.platform


class Student(_name: String, _age: Int, _address: Address)
  extends Human(_name, _age, _address) with Course {

  private var _tokens: Double = 0
  private var _enrolledCourses: Map[Course, Int] = Map.empty
  private var _gradebooks: List[Map[Int, List[Int]]] = Nil
  private var _scholarships: Map[Int, Double] = Map.empty

  def tokens: Double = _tokens
  def enrolledCourses: Map[Course, Int] = _enrolledCourses

  val courseID: Int = _enrolledCourses.headOption.map { case (course, _) => course.courseID }.getOrElse(0)
  val coursePrice: Int = _enrolledCourses.headOption.map { case (course, _) => course.coursePrice }.getOrElse(0)
  val courseDuration: Int = _enrolledCourses.headOption.map { case (course, _) => course.courseDuration }.getOrElse(0)
  val enrolledDuration: Int = _enrolledCourses.headOption.map { case (_, duration) => duration }.getOrElse(0)

  def getCourseByID(courseID: Int): Option[Course] = {
    _enrolledCourses.keys.find(_.courseID == courseID)
  }

  private def addCourse(courseID: Int): Unit = {
    _enrolledCourses = _enrolledCourses + (platform.getCourseByID(courseID).get -> 0)
    _gradebooks = _gradebooks :+ Map(courseID -> Nil)
    _scholarships = _scholarships + (courseID -> 0)
  }

  def addCoursesPeriod(): Unit = {
    _enrolledCourses = _enrolledCourses.view.mapValues(_ + 1).toMap
  }

  def addGrade(courseID: Int, grade: Int): Unit = {
    val course = getCourseByID(courseID)
    course.foreach { c =>
      if (_enrolledCourses.contains(c)) {
        val index = _gradebooks.indexWhere(_.contains(c.courseID))
        val currentGradebook = _gradebooks(index).getOrElse(c.courseID, Nil)
        val updatedGradebook = currentGradebook :+ grade
        _gradebooks = _gradebooks.updated(index, _gradebooks(index) + (c.courseID -> updatedGradebook))
      } else {
        println("Course not found")
      }
    }
  }


  def generalCoursesPrice(): Double = {
      _enrolledCourses.foldLeft(0.0) { case (total, (course, _)) =>
        total + course.coursePrice.toDouble
      }
  }

  def payment(): Unit = {
    val scholarshipAmount = calculateGeneralScholarship().getOrElse(0.0)
    _tokens += scholarshipAmount

    val coursesPrice = generalCoursesPrice()
    if (_tokens < coursesPrice) {
      buyTokens(coursesPrice - _tokens)
      _tokens = 0.0
    } else {
      _tokens -= coursesPrice
    }
  }

  def courseEND(courseID: Int): Unit = {
    val coaches = platform.officeList.flatMap(_.officeListCoaches)

    coaches.find(coach => coach.courseID == courseID) match {
      case Some(coach) =>
        _enrolledCourses.find { case (course, _) => course.courseID == courseID } match {
          case Some((studentCourse, studentCourseDuration)) =>
            if (coach.courseDuration == studentCourseDuration) {
              coach.removeStudent(this)
              _enrolledCourses = _enrolledCourses - studentCourse
              _gradebooks = _gradebooks.filterNot(_.contains(studentCourse.courseID))
              _scholarships = _scholarships - studentCourse.courseID
            }
          case None =>
            println("Course not found for student")
        }
      case None =>
        println(s"No coach found for course with ID: $courseID")
    }
  }

  def getLastGrade(courseID: Int): Option[Int] = {
    getCourseByID(courseID) match {
      case Some(course) =>
        _enrolledCourses.find { case (enrolledCourse, _) => enrolledCourse.courseID == course.courseID } match {
          case Some((_, courseDuration)) =>
            val courseIndex = _gradebooks.indexWhere(_.contains(course.courseID))
            if (courseIndex != -1) {
              val gradebook = _gradebooks(courseIndex).getOrElse(course.courseID, Nil)
              gradebook.lastOption
            } else {
              None
            }
          case None =>
            None
        }
      case None =>
        println(s"Course not found for courseID: $courseID")
        None
    }
  }

  def calculateScholarship(courseID: Int): Option[Double] = {
    getCourseByID(courseID) match {
      case Some(course) =>
        val grade = getLastGrade(course.courseID)
        val index = grade match {
          case Some(1) => Some(0.7)
          case Some(2) => Some(0.8)
          case Some(3) => Some(0.9)
          case Some(4) => Some(1.0)
          case Some(5) => Some(1.1)
          case _ => None
        }
        index.map(_ * course.coursePrice)
      case None =>
        println(s"Course not found for courseID: $courseID")
        None
    }
  }


  def calculateGeneralScholarship(): Option[Double] = {
    val scholarships = _enrolledCourses.flatMap { case (course, _) =>
      calculateScholarship(course.courseID)
    }

    if (scholarships.nonEmpty) {
      Some(scholarships.sum)
    } else {
      None
    }
  }

  def courseJoin(courseID: Int): Unit = {
    platform.getCourseByID(courseID) match {
      case Some(course) =>
        val coaches = platform.officeList.flatMap(_.officeListCoaches)
        val coachWithCourse = coaches.find(coach => coach.courseID == courseID)

        coachWithCourse match {
          case Some(coach) =>
            coach.addStudent(this)
            addCourse(course.courseID)
          case None =>
            println(s"No coach found for course with ID: $courseID")
        }
      case None =>
        println(s"No course found for ID: $courseID")
    }
  }


  def sellTokens(amount: Double): Unit = {
    if (amount > _tokens) {
      println("\nNot enough tokens")
    } else {
      _tokens -= amount
      platform.tokenSell(amount)
    }
  }

  def buyTokens(amount: Double): Unit = {
    _tokens += amount
    platform.tokenBuy(amount)
  }

  private def updateOther(): Unit = {
    platform.getRandomOffice match {
      case Some(office) =>
        office.addStudent(this)
      case None =>
        println("No offices available")
    }
  }


  override def toString: String = s"{${_name}, ${_age}, ${_address}, ${_tokens}, ${_enrolledCourses}}"
}