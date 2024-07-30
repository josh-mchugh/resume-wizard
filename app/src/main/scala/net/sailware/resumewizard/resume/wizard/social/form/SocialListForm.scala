package net.sailware.resumewizard.resume.wizard.social.form

case class SocialListForm(
  val entries: List[SocialUpdateForm],
  val newEntries: List[SocialAddForm]
)
