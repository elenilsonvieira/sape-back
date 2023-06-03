<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ativide Aprovada</title>
    
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            color: #333333;
        }

    h1 {
        font-family: Verdana, Geneva, Tahoma, sans-serif;
        color: #252726;
        margin-bottom: 20px;
    }
    
     .activity {
        border: 1px solid #0e0d0d;
        border-radius: 5px;
        padding: 20px;
        margin-bottom: 20px;
    }
    .activity-title {
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 10px;
    }

    .activity-details {
        font-size: 14px;
    }

   
    </style>
    
</head>
<body>
    <h1>Olá, ${name}!</h1>
    <p>Sua atividade foi aprovada pelo responsável do local.</p>
  
  <div class="activity">
  <h2 class="activity-title">Agendamento</h2>
    <p class="activity-details">Confira os detalhes:</p>
    <ul>
	        <li>Local: ${location}</li>      
	        <li>Esporte: ${sport}</li>      
	        <li>Data: ${date}</li>
	        <li>Horário: ${timeStart}</li>
	        <li>Término: ${timeFinish}</li>
	        <li>Responsável: ${nameResponsible}</li>
	        <li>Status: ${status}</li>
	
	    </ul>
  </div>
</body>
</html>