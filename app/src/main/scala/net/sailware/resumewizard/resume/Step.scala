package net.sailware.resumewizard.resume

enum Step(val label: String):
  case Detail extends Step("Details")
  case Social extends Step("Socials")
  case Experience extends Step("Experiences")
  case Skill extends Step("Skills")
  case Certification extends Step("Certifications")
  case Review extends Step("Review")
