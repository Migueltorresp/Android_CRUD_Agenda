# Android_CRUD_Agenda
El siguiente proyecto hace referencia a una agenda desarrollada en Android Studio (JAVA) y Firebase 
<br/><b>Video demostrativo:</b> https://youtu.be/Zsv784WBEUI
<br/><b>Realizado por:</b> Miguel Torres 
## PARTE 1 : Autenticación
<b>1. Conexión con Firebase </b>
 <br/>Para poder conectar el proyecto con Firebase es neceserio configurarlo mediante **Firebase Tools** dentro de Android Studio ingresando nuestras credenciales y vinculando a una base de datos previamente creada en Firebase, lo que nos genererar lo siguiente:<br/>
 ```python
 "project_info": {
    "project_number": "390958351514",
    "project_id": "androidfirebase-2d884",
    "storage_bucket": "androidfirebase-2d884.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:390958351514:android:aed3814dcf751b05b14b23",
        "android_client_info": {
          "package_name": "com.example.project_firebase"
        }
        .
        .
        .
```
<b>2. Metodo de autenticación </b>
<br/>El método de autencitacion levado a cabo es mediante correo electronico, este debe habilitarse en firebae y posteriormente ser configurado e implementado en el proyecto
para esto se utilizo la siguientes funciones:
### Dentro de *RegistrationActivity.java* 
```python
private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(RegistrationActivity.this, "Authentication Success." + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
```
### Dentro de *LoginActivity.java*
```python
private void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "Authentication Success." + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
```
## PARTE 2 : CRUD Agenda de estudiantes
<b>1. Funcionalidad </b>
<br/>Para realizar el crud se usó un modelo en este caso de persona, en donde constan los siguientes atributos:
```python
public class Persona {

    private String uid;
    private String Nombre;
    private String Apellido;
    private String Correo;
    .
    .
    .
```
Luego denreo de *HomeActivity.java* mediante iconos dentro del diseño de la aplicacion se procede a realizar las diferentes acciones mediante un *item selected* para crear, eliminar o actualizar un registro, como se muestra a continuacion:
```python
 public boolean onOptionsItemSelected(MenuItem item) {

        String nombre = nomP.getText().toString();
        String correo = correoP.getText().toString();
        //String password = passwordP.getText().toString();
        String app = appP.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if (nombre.equals("")||correo.equals("")||app.equals("")){
                    validacion();
                }
                else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(app);
                    p.setCorreo(correo);
                    //p.setPassword(password);
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save:{
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                p.setNombre(nomP.getText().toString().trim());
                p.setApellido(appP.getText().toString().trim());
                p.setCorreo(correoP.getText().toString().trim());
                //p.setPassword(passwordP.getText().toString().trim());
                databaseReference.child("Persona").child(p.getUid()).setValue(p);
                Toast.makeText(this,"Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_delete:{
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                databaseReference.child("Persona").child(p.getUid()).removeValue();
                Toast.makeText(this,"Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }
        return true;
    }
```
Por otro lado, para poder leer los registro se utilizo la siguiente función y posteriormente usando un *ListView*
```python
private void listarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPerson.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);
                    listPerson.add(p);

                    arrayAdapterPersona = new ArrayAdapter<Persona>(HomeActivity.this, android.R.layout.simple_list_item_1, listPerson);
                    listV_personas.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
```
<b>2. Diseño </b>
<b>El diseño consta de tres partes que son:
### Registro
Para el registro se agregó dos *EditText* para el correo y la constraseña con los que se podrá registrar un usuario dando como resultado lo siguiente:<br/>
 ![Screenshot](https://raw.github.com/Migueltorresp/Android_CRUD_Agenda/dev/images/registro.jpg)
### Login
Para el login de igual manera se agreagaron dos *EditText*, además de un aceeso hacia la ventana de registro en el caso que no se encuentre registrado:<br/>
 ![Screenshot](https://raw.github.com/Migueltorresp/Android_CRUD_Agenda/dev/images/iniciar_sesion.jpg)
### Home
 Para el diseño del *Home* tendremos los botones tanto de salir como de añadir, actualizar y eliminar un registro ademas de poder ver los regsitros guardados en una lista<br/>  
 ![Screenshot](https://raw.github.com/Migueltorresp/Android_CRUD_Agenda/dev/images/home.jpg)
 ### Dentro de la Firebase
Finalmente en *Realtime Database* se encuentran los registro creados.<br/> 
 ![Screenshot](https://raw.github.com/Migueltorresp/Android_CRUD_Agenda/dev/images/registro2.jpg)
  
