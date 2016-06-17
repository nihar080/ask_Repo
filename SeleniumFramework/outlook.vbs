'Option Explicit

set args = Wscript.Arguments
Attachment = args.Item(0)

TotalTestCases = args.Item(1)
PassedTestCases = args.Item(2)
FailedTestCases = args.Item(3)
TestEnvironment = args.Item(4)
MsgSub = args.Item(5)
ToAddr = args.Item(6)
'msgbox Attachment 
'msgbox TotalTestCases
'msgbox PassedTestCases
'msgbox FailedTestCases
'msgbox TestEnvironment
'msgbox MsgBody

' Get the WshShell object.
Set WshShell = CreateObject("WScript.Shell")

' Get collection by using the Environment property.
Set objEnv = WshShell.Environment("Process")
environment = objEnv("OS")

'msgbox arg1 & " " & arg2
        
    strHTML = "<html>"& _
               "<body>" & _ 
		"<p>Hi,<br><br>PFA detailed report, Please extract Report.zip file in C drive with 'Extract To Here...' option.Below is the summary of test execution -</p>" & _
		"<table border= 1><tr><td><b>Total Test Executed: " & TotalTestCases & "</b></td></tr><tr><td> Test Environment: " & TestEnvironment & "</td></tr><tr><td> Passed Tests: <b><FONT COLOR=008B00>" & PassedTestCases & "</b></td></tr><tr><td> Failed Tests: <b><FONT COLOR=FF0000>" & FailedTestCases & "</b></td></tr></table>" & _	
		"</body>" & _
           "</html>"







	'blnSuccessful = FnSafeSendEmail("ravi_p_singh@uhc.com", "My Message Subject", strHTML,"C:\Documents and Settings\rsin109\My Documents\DocGPSResult_Jul 4 2011 4_36_37 PM.csv,"","")    

   ' blnSuccessful = FnSafeSendEmail("ravi_p_singh@uhc.com", "My Message Subject", strHTML,"C:\Documents and Settings\rsin109\My Documents\DocGPSResult_Jul 4 2011 4_36_37 PM.csv;C:\DocGPS_Automation\DocGPSResult_Jul 2, 2011 1_43_18 PM.txt","","")    

   blnSuccessful = FnSafeSendEmail(toAddr, MsgSub, strHTML,Attachment,"","") 

    'A more complex example...
    'blnSuccessful = FnSafeSendEmail( _
'                        "myemailaddress@domain.com; recipient2@domain.com", _
 '                       "My Message Subject", _
  '                      strHTML, _
   '                     "C:\MyAttachFile1.txt; C:\MyAttachFile2.txt", _
    '                    "cc_recipient@domain.com", _
     '                   "bcc_recipient@domain.com")

    If blnSuccessful Then
    
        MsgBox "E-mail message sent successfully!"
        
    Else
    
        MsgBox "Failed to send e-mail!"
    
    End If




'This is the procedure that calls the exposed Outlook VBA function...
Public Function FnSafeSendEmail(strTo,strSubject,strMessageBody,strAttachmentPaths,strCC,strBCC )

'    Dim objOutlook As Object ' Note: Must be late-binding.
 '   Dim objNameSpace As Object
   ' Dim objExplorer As Object
    'Dim blnSuccessful As Boolean
    'Dim blnNewInstance As Boolean
    
    'Is an instance of Outlook already open that we can bind to?
    On Error Resume Next
    Set objOutlook = GetObject(, "Outlook.Application")
    On Error GoTo 0
    
    If objOutlook Is Nothing Then
    
        'Outlook isn't already running - create a new instance...
        Set objOutlook = CreateObject("Outlook.Application")
        blnNewInstance = True
        'We need to instantiate the Visual Basic environment... (messy)
        Set objNameSpace = objOutlook.GetNamespace("MAPI")
        Set objExplorer = objOutlook.Explorers.Add(objNameSpace.Folders(1), 0)
        objExplorer.CommandBars.FindControl(, 1695).Execute
                
        objExplorer.Close
                
        Set objNameSpace = Nothing
        Set objExplorer = Nothing
        
    End If
	'msgbox strTo
	'strTo=trim(cstr(strTo))
	'strTo= cstr("pankaj_behl@uhc.com")
    bln= objOutlook.FnSendMailSafe(strTo,strCC,strBCC,strSubject,strMessageBody,strAttachmentPaths)
                                
    If blnNewInstance = True Then objOutlook.Quit
    Set objOutlook = Nothing
    
    FnSafeSendEmail = bln
    
End Function
