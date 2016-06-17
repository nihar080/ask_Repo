set args = Wscript.Arguments
Attachment = args.Item(0)

TotalTestCases = args.Item(1)
PassedTestCases = args.Item(2)
FailedTestCases = args.Item(3)
TestEnvironment = args.Item(4)
MsgSub = args.Item(5)
ToAddr = args.Item(6)


Set WshShell = CreateObject("WScript.Shell")


Set objEnv = WshShell.Environment("Process")

environment = objEnv("OS")

        
    strHTML = "<html>"& _
               "<body>" & _ 
		"<p>Hi,<br><br>Please Find Attached the detailed report, Please extract Report.zip file in C drive with 'Extract To Here...' option. <br><br> Below is the summary of test execution -</p>" & _
		"<table border= 1><tr><td><b>Total Test Executed: " & TotalTestCases & "</b></td></tr><tr><td> Browser : " & TestEnvironment & "</td></tr><tr><td> Passed Tests: <b><FONT COLOR=008B00>" & PassedTestCases & "</b></td></tr><tr><td> Failed Tests: <b><FONT COLOR=FF0000>" & FailedTestCases & "</b></td></tr></table>" & _	
		"</body>" & _
           "</html>"


  MailAddrSplit = Split(ToAddr, ";")
  
 Set out = CreateObject("Outlook.Application")
 Set mapi = out.GetNameSpace("MAPI")
 Set Email = out.CreateItem(0)
 
  For mailaddrcnts = LBound(MailAddrSplit) To UBound(MailAddrSplit)
  Email.Recipients.Add (MailAddrSplit(mailaddrcnts))
 Next
 
 Email.Subject = MsgSub &"_" & now
 'Email.Body = strHTML
Email.HTMLBody = strHTML
    
 Set oAttachment = Email.Attachments.Add(Attachment)
 Email.Send
  
 Set oAttachment = Nothing
 Set outlook = Nothing
 Set mapi = Nothing
 Set out = Nothing

'End Function