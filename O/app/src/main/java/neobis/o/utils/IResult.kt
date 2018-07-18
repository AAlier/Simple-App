package neobis.alier.bilimkana.util

interface IResult<T> {
    fun onSuccess(result: T)
    fun onError(message: String?)
}