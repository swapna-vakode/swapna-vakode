from flask import Flask, render_template, request, redirect, url_for, session
from flask_mysqldb import MySQL
from flask_mail import *  
from random import * 
import MySQLdb.cursors
import re

app = Flask(__name__)

app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = ''
app.config['MYSQL_DB'] = 'testingDb'

mysql = MySQL(app)

mail = Mail(app)  
app.config["MAIL_SERVER"]='smtp.gmail.com'  
app.config["MAIL_PORT"] = 465      
app.config["MAIL_USERNAME"] = 'username@gmail.com'  
app.config['MAIL_PASSWORD'] = '*************'  
app.config['MAIL_USE_TLS'] = False  
app.config['MAIL_USE_SSL'] = True  
mail = Mail(app)  
otp = randint(000000,999999) 

# http://localhost:5000/pythonlogin/ - the login page
@app.route('/pythonlogin/', methods=['GET', 'POST'])
def login():
    msg = ''
    if request.method == 'POST' and 'username' in request.form and 'password' in request.form:
        username = request.form['username']
        password = request.form['password']
        cursor = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
        cursor.execute('SELECT * FROM accounts WHERE username = %s AND password = %s', (username, password,))
        account = cursor.fetchone()
        if account:
            session['loggedin'] = True
            session['id'] = account['id']
            session['email'] = account['email']
            session['username'] = account['username']
            return redirect(url_for('home'))
        else:
            msg = 'Incorrect username/password!'
    return render_template('index.html', msg=msg)

# http://localhost:5000/python/logout - the logout page
@app.route('/pythonlogin/logout')
def logout():
   session.pop('loggedin', None)
   session.pop('id', None)
   session.pop('username', None)
   return redirect(url_for('login'))

# http://localhost:5000/pythinlogin/register - the registration page
@app.route('/pythonlogin/register', methods=['GET', 'POST'])
def register():
    msg = ''
    if request.method == 'POST' and 'username' in request.form and 'password' in request.form and 'email' in request.form:
        username = request.form['username']
        password = request.form['password']
        email = request.form['email']
    
        cursor = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
        cursor.execute('SELECT * FROM accounts WHERE username = %s', (username,))
        account = cursor.fetchone()
        
        if account:
            msg = 'Account already exists!'
        elif not re.match(r'[^@]+@[^@]+\.[^@]+', email):
            msg = 'Invalid email address!'
        elif not re.match(r'[A-Za-z0-9]+', username):
            msg = 'Username must contain only characters and numbers!'
        elif not username or not password or not email:
            msg = 'Please fill out the form!'
        else:
            cursor.execute('INSERT INTO accounts VALUES (NULL, %s, %s, %s)', (username, password, email,))
            mysql.connection.commit()
            msg = 'You have successfully registered!'
    elif request.method == 'POST':
        msg = 'Please fill out the form!'
    return render_template('register.html', msg=msg)

# http://localhost:5000/pythinlogin/home - the home/dashboard page
@app.route('/pythonlogin/home')
def home():
    if 'loggedin' in session:
        return render_template('home.html', username=session['username'])
    return redirect(url_for('login'))

# http://localhost:5000/pythinlogin/profile - the profile page
@app.route('/pythonlogin/profile')
def profile():
    if 'loggedin' in session:
        cursor = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
        cursor.execute('SELECT * FROM accounts WHERE id = %s', (session['id'],))
        account = cursor.fetchone()
        return render_template('profile.html', account=account)
    return redirect(url_for('login'))

@app.route('/pythonlogin/verify')
def verify():
    email = request.form["email"]
    msg = Message('OTP',sender='username@gmail.com',recipients=[email])
    msg.body = str(otp)
    mail.send(msg)
    return render_template('verify.html', username=session['username'])

@app.route('/pythonlogin/forgotPassword')
def forgotPassword():
    email = request.form["email"]
    msg = Message('OTP',sender='username@gmail.com',recipients=[email])
    msg.body = str(otp)
    mail.send(msg)
    session['email'] = email
    return render_template('forgotPassword.html', username=session['username'])

@app.route('/pythonlogin/validate', methods=["POST"]) 
def validate():
    user_otp = request.form['otp']
    if otp == int(user_otp):
        return "<h3> Email verification is successful </h3>"
    else : 
        "<h3>failure, OTP does not match</h3>"
    return redirect(url_for('login'))  

@app.route('/pythonlogin/updateEmail', methods=["POST"]) 
def updateEmail():
    if 'loggedin' in session:
        email = session['email']
        cursor = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
        cursor.execute('UPDATE accounts SET email = %s WHERE id = %s', (email, session['id'], ))
        mysql.connection.commit()
        return "<h3>Email Updated successfully</h3>"
    else: 
        return redirect(url_for('login')) 
    
@app.route('/pythonlogin/resetPassword', methods=["POST"]) 
def resetPassword():
    email = session['email']
    password = request.form["password"]
    cursor = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
    cursor.execute('UPDATE accounts SET password = %s WHERE id = %s', (password, email, ))
    mysql.connection.commit()
    msg = "Password reset successfully"
    return redirect(url_for('forgetPassword.html',msg=msg)) 


if __name__ == '__main__':
    app.run(debug=True)