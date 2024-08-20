/*La classe GlobalExceptionHandler centralise la gestion des exceptions en fournissant des méthodes pour capturer et
traiter différentes erreurs au sein de l'application.

Pour gérer les erreurs pour les ressources non trouvées par exemple, il faut ajouter à la classe GlobalExceptionHandler
une méthode générale pour les ressources non trouvées que nous appellerons handleResourceNotFound */

package org.myblognew.MyBlogNew.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    /*L'annotation @ExceptionHandler indique à Spring que cette méthode doit être exécutée lorsque l'application rencontre
    une exception du type précisé en paramètre, ici ResourceNotFoundException.

La méthode handleResourceNotFound retourne une ResponseEntity avec un code status 404 ainsi qu'un message qui sera
défini lors de la levée de l'exception.*/


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue");
    }
/*Pour gérer toutes les autres exceptions non spécifiées dans d'autres méthodes @ExceptionHandler,
ajouter une méthode handleGlobalException à la classe GlobalExceptionHandler :

Si une exception inattendue survient dans l'application, cette méthode garantit qu'une réponse appropriée est envoyée au client,
même si l'erreur spécifique n'a pas été prévue.*/

}
