import Main.platform
import scala.util.Random


class Simulation {
  def newMonth():Unit = {
    val students = platform.officeList.flatMap(_.officeListStudents)
    val coaches = platform.officeList.flatMap(_.officeListCoaches)

    students.foreach(student => {
      student.enrolledCourses.foreach(course => {
        var courseID =course._1.courseID
        val randomGrade = scala.util.Random.nextInt(5) + 1
        student.addGrade(courseID, randomGrade)
        student.courseEND(courseID)
      })

      student.addCoursesPeriod()
      student.payment()
    })

    coaches.foreach(coach => {
      coach.addTrainingPeriod()
      coach.payment()
    })

    platform.calculateTokenPrice()
  }
}
