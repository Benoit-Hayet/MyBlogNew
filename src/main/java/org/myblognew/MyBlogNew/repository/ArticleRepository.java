/*Un repository est une interface qui permet de définir les méthodes d'accès aux données pour une entité.
Ici nous appelons l'entité article*/

package org.myblognew.MyBlogNew.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.myblognew.MyBlogNew.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}


/*Comme l'interface JpaRepository hérite de PagingAndSortingRepository, lui-même enfant de CrudRepository, cela signifie simplement
 que ArticleRepository hérite des méthodes CRUD de base et d'autres fonctionnalités avancées par défaut :

List<Article> findAll() : Récupère tous les articles.
<Article> findById(Long id) : Récupère un article par son identifiant.
<Article> save(article) : Sauvegarde un article (crée ou met à jour).
void deleteById(Long id) : Supprime un article par son identifiant.

Ces méthodes nous permettent de réaliser les opérations CRUD sans avoir à les implémenter nous-mêmes.


Spring Data JPA offre d'autres méthodes de requête directement dans les interfaces de repository.
Ces méthodes suivent une convention de nommage spécifique qui commence généralement par un verbe comme find, read, get,
puis est suivie par des mots clés décrivant les critères de la requête. */