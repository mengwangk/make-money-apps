<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>myMobKit</title>

<%@ include file="header.jsp" %>

</head>

<body data-spy="scroll">

<%@ include file="common.jsp" %>

<!-- MAIN CONTENT -->
<div class="container content container-fluid" id="home">



	<!-- HOME -->
	<div class="row-fluid">
  
		<!-- PHONES IMAGE FOR DESKTOP MEDIA QUERY -->
		<div class="span5 visible-desktop">
			<img src="img/phones.png">
		</div>
	
		<!-- APP DETAILS -->
		<div class="span7">
	
			<!-- ICON -->
			<div class="visible-desktop" id="icon">
				<img src="img/app_icon.png" />
			</div>
			
			<!-- APP NAME -->
			<div id="app-name">
				<h1>myMobKit</h1>
			</div>
			
			<!-- VERSION -->
			<div id="version">
				<span class="version-top label label-inverse">Version 0.1</span>
			</div>
            
			<!-- TAGLINE -->
			<div id="tagline">
				Access Your Device Anywhere
			</div>
		
			<!-- PHONES IMAGE FOR TABLET MEDIA QUERY -->
			<div class="hidden-desktop" id="phones">
				<img src="img/phones.png">
			</div>
            
			<!-- DESCRIPTION -->
			<div id="description">
				myMobKit is an Android phone application
			</div>
            
			<!-- FEATURES -->
			<ul id="features">
				<li>Turn your smart phone into a surveillance camera</li>
				<li>Use your phone as a SMS gateway</li>
				<li>Completely Free!</li>
			</ul>
		
			<!-- DOWNLOAD & REQUIREMENT BOX -->
			
			<div class="download-box">
				<a href="#"><img src="img/android_app_on_play_logo_large.png"></a>
				
				<br/><br/>
				<strong>Requirements:</strong><br>
				Requires Android 2.3 and higher. WiFi, Edge, or 3G network connection sometimes required.
			</div>
			
		</div>
	</div>
	
	
	
	<!-- ABOUT & UPDATES -->
	<div class="row-fluid" id="about">
	
		<div class="span6">
			<h2 class="page-title" id="scroll_up">
				About
				<a href="#home" class="arrow-top">
				<img src="img/arrow-top.png">
				</a>
			</h2>
			
			<p>
			
			myMobKit is a 
			
			</p>
			
		</div>
	
		<div class="span6 updates" id="updates">
			<h2 class="page-title" id="scroll_up">
				Updates
				<a href="#home" class="arrow-top">
				<img src="img/arrow-top.png">
				</a>
			</h2>
			
			<!-- UPDATES & RELEASE NOTES -->
		
			<h3 class="version">Version 0.1</h3>
			<span class="release-date">Released on Dec 10th, 2013</span>
			<ul>
				<li><span class="label label-info">NEW</span>Initial Release</li>
			</ul>			
		</div>	
	</div>
	
	
	<!-- SCREENSHOTS -->
	<div class="row-fluid" id="screenshots">
		
		<h2 class="page-title" id="scroll_up">
				Screenshots
				<a href="#home" class="arrow-top">
				<img src="img/arrow-top.png">
				</a>
			</h2>
		
		<!-- SCREENSHOT IMAGES ROW 1-->
		<ul class="thumbnails">
			<li class="span3">
				<a href="img/screenshot.jpg" rel="gallery" class="thumbnail">
				<img src="img/screenshot.jpg" alt="">
				</a>
			</li>
		
			<li class="span3">
				<a href="img/screenshot.jpg" rel="gallery" class="thumbnail">
				<img src="img/screenshot.jpg" alt="">
				</a>
			</li>
			
			<li class="span3">
				<a href="img/screenshot.jpg" rel="gallery" class="thumbnail">
				<img src="img/screenshot.jpg" alt="">
				</a>
			</li>
 
			<li class="span3">
				<a href="img/screenshot.jpg" rel="gallery" class="thumbnail">
				<img src="img/screenshot.jpg" alt="">
				</a>
			</li>
		</ul>	
		
		<!-- SCREENSHOT IMAGES ROW 2-->		
		<ul class="thumbnails">
			<li class="span3">
				<a href="img/screenshot.jpg" rel="gallery" class="thumbnail">
				<img src="img/screenshot.jpg" alt="">
				</a>
			</li>
			
			<li class="span3">
				<a href="img/screenshot.jpg" rel="gallery" class="thumbnail">
				<img src="img/screenshot.jpg" alt="">
				</a>
			</li>
			
			<li class="span3">
				<a href="img/screenshot.jpg" rel="gallery" class="thumbnail">
				<img src="img/screenshot.jpg" alt="">
				</a>
			</li>
			
			<li class="span3">
				<a href="img/screenshot.jpg" rel="gallery" class="thumbnail">
				<img src="img/screenshot.jpg" alt="">
				</a>
			</li>
		</ul>
	</div>
	
	
	
	<!-- CONTACT -->
	<div class="row-fluid" id="contact">
	
		<h2 class="page-title" id="scroll_up">
				Contact
				<a href="#home" class="arrow-top">
				<img src="img/arrow-top.png">
				</a>
			</h2>
		
		<!-- CONTACT INFO -->
		<div class="span4" id="contact-info">
			<h3>Contact Us</h3>
			<p>You can contact us</p>
			
		</div>
		
		<!-- CONTACT FORM -->
		<div class="span7" id="contact-form">
			<form class="form-horizontal">
				<fieldset>
					<div class="control-group">
						<label class="control-label" for="name">Name</label>
						<div class="controls">
							<input class="input-xlarge" type="text" id="name" placeholder="John Doe">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="email">Email</label>
						<div class="controls">
							<input class="input-xlarge" type="text" id="email" placeholder="john@gmail.com">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="subject">Subject</label>
						<div class="controls">
							<input class="input-xlarge" type="text" id="subject" placeholder="General Inquiry">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="message">Message</label>
						<div class="controls">
							<textarea class="input-xlarge" rows="3" id="message" placeholder="Your message..."></textarea>
						</div>
					</div>
					<div class="form-actions">
						<button type="submit" class="btn btn-primary">SEND</button>
					</div>
				</fieldset>
			</form>
		</div>
		
	</div>
	
</div>


<%@ include file="footer.jsp" %>

</body>
</html>
