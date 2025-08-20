package com.example.treeapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.treeapp.data.db.entity.NodeClosureEntity
import com.example.treeapp.data.db.entity.NodeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface NodeDao {

    /**
     * Вставляет новую ноду в таблицу `nodes`.
     *
     * @param node Объект `NodeEntity`, представляющий ноду для вставки.
     * @return `Long` ID вставленной ноды (ID строки в таблице).  Это сгенерированный ID.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNode(node: NodeEntity): Long


    /**
     * Удаляет ноду из таблицы `nodes`.
     *
     * @param node Объект `NodeEntity`, представляющий ноду для удаления.  `id` должен соответствовать существующему `id` в таблице.
     */
    @Delete
    suspend fun deleteNode(node: NodeEntity)


    /**
     * Получает ноду из таблицы `nodes` по ее ID.
     *
     * @param id ID ноды для поиска.
     * @return `NodeEntity?` Объект `NodeEntity`, представляющий ноду с указанным ID, или `null`, если нода с таким ID не найдена.
     */
    @Query("SELECT * FROM nodes WHERE id = :id")
    suspend fun getNodeById(id: Long): NodeEntity?


    /**
     * Получает Root ноду, у которой нет родителя.
     * Возвращает null, если корневых нод нет.
     *
     * @return `NodeEntity?` Объект `NodeEntity`, представляющий Root ноду, или `null`, если корневых нод нет.
     */
    @Query("SELECT n.* FROM nodes n WHERE NOT EXISTS ( SELECT 1 FROM node_closure nc WHERE nc.descendant_id = n.id AND nc.depth = 1)")
    suspend fun getRootNode(): NodeEntity?


    /**
     * Вставляет новую запись в таблицу `node_closure`, представляющую связь предок-потомок.
     *
     * @param closure Объект `NodeClosureEntity`, представляющий связь предок-потомок для вставки.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNodeClosure(closure: NodeClosureEntity)


    /**
     * Удаляет запись из таблицы `node_closure`.
     *
     * @param closure Объект `NodeClosureEntity`, представляющий связь предок-потомок для удаления.
     */
    @Delete
    suspend fun deleteNodeClosure(closure: NodeClosureEntity)


    /**
     * Удаляет все записи из таблицы `node_closure`, связанные с указанным ID ноды как потомком.
     * Это используется при удалении ноды, чтобы удалить все ее связи.
     *
     * @param nodeId ID ноды, для которой нужно удалить связи.
     */
    @Query("DELETE FROM node_closure WHERE descendant_id = :nodeId")
    suspend fun deleteNodeClosuresForNode(nodeId: Long)


    /**
     * Получает всех потомков указанного узла.
     *
     * @param ancestorId ID узла, для которого нужно получить потомков.
     * @return `List<NodeEntity>` Список объектов `NodeEntity`, представляющих всех потомков указанного узла.
     */
    @Query("SELECT n.* FROM nodes n JOIN node_closure nc ON n.id = nc.descendant_id WHERE nc.ancestor_id = :ancestorId")
    suspend fun getDescendants(ancestorId: Long): List<NodeEntity>


    /**
     * Получает всех предков указанного узла.
     *
     * @param descendantId ID узла, для которого нужно получить потомков.
     * @return `List<NodeEntity>` Список объектов `NodeEntity`, представляющих всех потомков указанного узла.
     */
    @Query("SELECT n.* FROM nodes n JOIN node_closure nc ON n.id = nc.ancestor_id WHERE nc.descendant_id = :descendantId")
    suspend fun getAncestors(descendantId: Long): List<NodeEntity>

    /**
     * Получает всех непосредственных детей (прямых потомков) указанного узла.
     *
     * @param parentId ID узла, для которого нужно получить детей.
     * @return `Flow<List<NodeEntity>>` Flow со списком объектов `NodeEntity`, представляющих всех детей указанного узла.
     */
    @Query("SELECT n.* FROM nodes n JOIN node_closure nc ON n.id = nc.descendant_id WHERE nc.ancestor_id = :parentId AND nc.depth = 1")
    fun getChildren(parentId: Long): Flow<List<NodeEntity>>


    /**
     * Получает непосредственного родителя указанного узла.
     *
     * @param childId ID узла, для которого нужно получить родителя.
     * @return `Long?` ID родителя указанного узла, или `null`, если у узла нет родителя.
     */
    @Query("SELECT n.id FROM nodes n JOIN node_closure nc ON n.id = nc.ancestor_id WHERE nc.descendant_id = :childId AND nc.depth = 1")
    suspend fun getParentId(childId: Long): Long?

    /**
     * Получает глубину (расстояние) между двумя узлами в дереве.
     *
     * @param parentId ID узла-предка.
     * @param childId ID узла-потомка.
     * @return `Int?` Глубина между предком и потомком, или `null`, если между ними нет связи.
     */
    @Query("SELECT nc.depth FROM node_closure nc WHERE nc.ancestor_id = :parentId AND nc.descendant_id = :childId")
    suspend fun getDepth(parentId: Long, childId: Long): Int?

    /**
     * Вставляет новую ноду в дерево и создает все необходимые записи в таблице `node_closure`.
     *
     * @param node Объект `NodeEntity`, представляющий ноду для вставки.
     * @param parentId ID родительской ноды. Если `null`, нода добавляется как корневая.
     */
    @Transaction
    suspend fun insertNodeWithClosure(node: NodeEntity, parentId: Long?) {
        val nodeId = insertNode(node)

        insertNodeClosure(NodeClosureEntity(nodeId, nodeId, 0))

        if (parentId != null) {
            val parents = getAncestors(parentId)

            for (parent in parents) {
                val parentDepth = getDepth(parent.id, parentId) ?: 0
                insertNodeClosure(NodeClosureEntity(parent.id, nodeId, parentDepth + 1))
            }

            insertNodeClosure(NodeClosureEntity(parentId, nodeId, 1))
        }
    }

    /**
     * Рекурсивно удаляет ноду и всех ее потомков из дерева.
     * Это включает удаление ноды из таблицы `nodes` и всех соответствующих
     * записей из таблицы `node_closure`.
     *
     * @param nodeId ID ноды, которую нужно удалить вместе со всеми ее потомками.
     */
    @Transaction
    suspend fun deleteNodeAndClosure(nodeId: Long) {
        val children = getChildren(nodeId).first()

        for (child in children) {
            deleteNodeAndClosure(child.id) // Рекурсивный вызов для удаления потомка
        }

        deleteNodeClosuresForNode(nodeId)

        val node = getNodeById(nodeId)
        node?.let {
            deleteNode(it)
        }
    }
}