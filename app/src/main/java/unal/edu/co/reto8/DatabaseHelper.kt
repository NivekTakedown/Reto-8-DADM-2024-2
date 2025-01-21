package unal.edu.co.reto8

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "EmpresasDB"
        const val DATABASE_VERSION = 1

        // Nombre de la tabla y columnas
        const val TABLE_EMPRESAS = "Empresas"
        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_URL = "url"
        const val COL_TELEFONO = "telefono"
        const val COL_EMAIL = "email"
        const val COL_PRODUCTOS = "productos_servicios"
        const val COL_CLASIFICACION = "clasificacion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_EMPRESAS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_URL TEXT,
                $COL_TELEFONO TEXT,
                $COL_EMAIL TEXT,
                $COL_PRODUCTOS TEXT,
                $COL_CLASIFICACION TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMPRESAS")
        onCreate(db)
    }

    // **Insertar una nueva empresa**
    fun insertEmpresa(empresa: Empresa): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE, empresa.nombre)
            put(COL_URL, empresa.url)
            put(COL_TELEFONO, empresa.telefono)
            put(COL_EMAIL, empresa.email)
            put(COL_PRODUCTOS, empresa.productosServicios)
            put(COL_CLASIFICACION, empresa.clasificacion)
        }
        return db.insert(TABLE_EMPRESAS, null, values)
    }

    // **Leer todas las empresas**
    fun getAllEmpresas(): List<Empresa> {
        val db = readableDatabase
        val empresas = mutableListOf<Empresa>()
        val cursor = db.query(TABLE_EMPRESAS, null, null, null, null, null, "$COL_NOMBRE ASC")

        with(cursor) {
            while (moveToNext()) {
                empresas.add(
                    Empresa(
                        id = getInt(getColumnIndexOrThrow(COL_ID)),
                        nombre = getString(getColumnIndexOrThrow(COL_NOMBRE)),
                        url = getString(getColumnIndexOrThrow(COL_URL)),
                        telefono = getString(getColumnIndexOrThrow(COL_TELEFONO)),
                        email = getString(getColumnIndexOrThrow(COL_EMAIL)),
                        productosServicios = getString(getColumnIndexOrThrow(COL_PRODUCTOS)),
                        clasificacion = getString(getColumnIndexOrThrow(COL_CLASIFICACION))
                    )
                )
            }
            close()
        }
        return empresas
    }

    // **Actualizar una empresa**
    fun updateEmpresa(empresa: Empresa): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE, empresa.nombre)
            put(COL_URL, empresa.url)
            put(COL_TELEFONO, empresa.telefono)
            put(COL_EMAIL, empresa.email)
            put(COL_PRODUCTOS, empresa.productosServicios)
            put(COL_CLASIFICACION, empresa.clasificacion)
        }
        return db.update(TABLE_EMPRESAS, values, "$COL_ID = ?", arrayOf(empresa.id.toString()))
    }

    // **Eliminar una empresa**
    fun deleteEmpresa(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_EMPRESAS, "$COL_ID = ?", arrayOf(id.toString()))
    }

    // **Obtener una empresa por ID**
    fun getEmpresaById(id: Int): Empresa? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_EMPRESAS,
            null,
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val empresa = Empresa(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)),
                url = cursor.getString(cursor.getColumnIndexOrThrow(COL_URL)),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow(COL_TELEFONO)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                productosServicios = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRODUCTOS)),
                clasificacion = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLASIFICACION))
            )
            cursor.close()
            empresa
        } else {
            cursor.close()
            null
        }
    }
}

// **Clase modelo para Empresa**
data class Empresa(
    val id: Int = 0,
    val nombre: String,
    val url: String?,
    val telefono: String?,
    val email: String?,
    val productosServicios: String?,
    val clasificacion: String?
)
